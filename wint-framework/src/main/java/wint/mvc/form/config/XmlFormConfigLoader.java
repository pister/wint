package wint.mvc.form.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wint.core.config.Configuration;
import wint.core.config.Constants;
import wint.core.io.resource.Resource;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.parser.XMLParseUtil;
import wint.lang.exceptions.FormConfigException;
import wint.lang.magic.MagicMap;
import wint.lang.template.SimpleTemplateEngine;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.i18n.I18nResourceFinder;

/**
 * @author pister 2012-2-8 04:51:44
 */
public class XmlFormConfigLoader implements FormConfigLoader {

	private ResourceLoader resourceLoader;
	
	private SimpleTemplateEngine simpleTemplateEngine;
	
	private XPathExpression formExpr;
	
	private XPathExpression formFieldExpr;
	
	private XPathExpression formFieldValidatorExpr;
	
	private XPathExpression formFieldValidatorParamExpr;
	
	private XPathExpression resourceExpr;

	private Configuration configuration;

	private I18nResourceFinder i18nResourceFinder;
	
	public XmlFormConfigLoader(ResourceLoader resourceLoader, SimpleTemplateEngine simpleTemplateEngine, Configuration configuration) {
		super();
		this.resourceLoader = resourceLoader;
		this.simpleTemplateEngine = simpleTemplateEngine;
		this.configuration = configuration;
		init();
	}
	
	private void init() {
		try {
			i18nResourceFinder = new I18nResourceFinder();
			String i18nResource = configuration.getProperties().getString(Constants.PropertyKeys.APP_I18N, Constants.Defaults.APP_I18N);
			i18nResourceFinder.loadResources(i18nResource);

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			formExpr = xpath.compile("/forms/form");
			formFieldExpr = xpath.compile("field");
			formFieldValidatorExpr = xpath.compile("validator");
			formFieldValidatorParamExpr = xpath.compile("param");
			resourceExpr = xpath.compile("/forms/resource");
			resourceExpr = xpath.compile("/forms/resource");
		} catch (Exception e) {
			throw new FormConfigException(e);
		}
	}

	public ParseResult parse(String name) {
		Map<String, FormConfig> formConfigs = MapUtil.newHashMap();
		Set<String> resourceNames = CollectionUtil.newHashSet();
		resourceNames.add(name);
		parseImpl(name, formConfigs, resourceNames);
		processFormsInherit(formConfigs);
		return new ParseResult(formConfigs, resourceNames);
	}
	
	private void processFormsInherit(Map<String, FormConfig> formConfigs) {
		Set<String> hasBeenProcessFormNames = CollectionUtil.newHashSet();
		for (Map.Entry<String, FormConfig> entry : formConfigs.entrySet()) {
			FormConfig formConfig = entry.getValue();
			processForExtends(formConfig, formConfigs, hasBeenProcessFormNames);
		}
	}
	
	private void checkExtendsRecursion(FormConfig formConfig, Map<String, FormConfig> formConfigs) {
		String name = formConfig.getName();
		Set<String> names = CollectionUtil.newHashSet();
		names.add(name);
		while (true) {
			String extendsFormName = formConfig.getExtendsFormName();
			if (StringUtil.isEmpty(extendsFormName)) {
				return;
			}
			if (names.contains(extendsFormName)) {
				throw new FormConfigException("there are Inherit Recursion in form \""+ name +"\".");
			}
			FormConfig parentFormConfig = formConfigs.get(extendsFormName);
			if (parentFormConfig == null) {
				return;
			}
			names.add(parentFormConfig.getName());
			formConfig = parentFormConfig;
		}
	}
	
	private void processForExtends(FormConfig formConfig, Map<String, FormConfig> formConfigs, Set<String> hasBeenProccessFormNames) {
		checkExtendsRecursion(formConfig, formConfigs);
		String extendsFormName = formConfig.getExtendsFormName();
		if (StringUtil.isEmpty(extendsFormName)) {
			return;
		}
		if (hasBeenProccessFormNames.contains(formConfig.getName())) {
			return;
		}
		FormConfig parentFormConfig = formConfigs.get(extendsFormName);
		if (parentFormConfig == null) {
			throw new FormConfigException("in \""+ formConfig.getName() +"\", the parent form \""+ extendsFormName +"\" not exist.");
		}
		if (!hasBeenProccessFormNames.contains(extendsFormName)) {
			processForExtends(parentFormConfig, formConfigs, hasBeenProccessFormNames);
		}
		// process me!
		processFormConfig(formConfig, parentFormConfig);
		hasBeenProccessFormNames.add(formConfig.getName());
	}
	
	private void processFormConfig(FormConfig currentFormConfig, FormConfig parentFormConfig) {
		Map<String, FieldConfig> fieldConfigs = MapUtil.newHashMap(parentFormConfig.getFieldConfigs());
		fieldConfigs.putAll(currentFormConfig.getFieldConfigs());
		currentFormConfig.setFieldConfigs(fieldConfigs);
	}
	
	private boolean parseImpl(String name, Map<String, FormConfig> formConfigs, Set<String> resourceNames) {
		resourceNames.add(name);
		Resource resource = resourceLoader.getResource(name);
		if (resource == null || !resource.exist()) {
			throw new FormConfigException("can not find form resource: " + name);
		}
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		try {
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
			InputStream is = resource.getInputStream();
			Document document = builder.parse(is);
			
			NodeList forms = (NodeList)formExpr.evaluate(document, XPathConstants.NODESET);
			NodeList resources = (NodeList)resourceExpr.evaluate(document, XPathConstants.NODESET);
			evalForms(forms, formConfigs);
			evalResources(resources, formConfigs, resourceNames);
		} catch (Exception e) {
			throw new FormConfigException(e);
		}
		return true;
	}
	
	private void evalForms(NodeList forms, Map<String, FormConfig> formConfigs) throws XPathExpressionException {
		if (forms == null) {
			return;
		}
		for (int i = 0, len = forms.getLength(); i < len; ++i) {
			Node formNode = forms.item(i);
			String name = XMLParseUtil.getAttribute(formNode, "name");
			if (StringUtil.isEmpty(name)) {
				throw new FormConfigException("form\'s name can not be empty!");
			}
			if (formConfigs.containsKey(name)) {
				throw new FormConfigException("form\'s name \""+ name +"\" cat not be duplication!");
			}
			
			FormConfig formConfig = new FormConfig();
			formConfig.setName(name);
			String extendsFormName = XMLParseUtil.getAttribute(formNode, "extends");
			if (!StringUtil.isEmpty(extendsFormName)) {
				formConfig.setExtendsFormName(extendsFormName);
			}
			NodeList fields = (NodeList)formFieldExpr.evaluate(formNode, XPathConstants.NODESET);
			evalFields(fields, formConfig);
			formConfigs.put(name, formConfig);
		}
	}
	
	private void evalFields(NodeList fields, FormConfig formConfig) throws XPathExpressionException {
		if (fields == null) {
			return;
		}
		for (int i = 0, len = fields.getLength(); i < len; ++i) {
			Node fieldNode = fields.item(i);
			String name = XMLParseUtil.getAttribute(fieldNode, "name");
			if (StringUtil.isEmpty(name)) {
				throw new FormConfigException("form[\""+ formConfig.getName() +"\"] has a field whose name is empty!");
			}
			if (formConfig.getFieldConfigs().containsKey(name)) {
				throw new FormConfigException("form[\""+ formConfig.getName() +"\"] has more than one fields which name is \""+ name +"\"!");
			}
			FieldConfig fieldConfig = new FieldConfig();
			fieldConfig.setFormConfig(formConfig);
			fieldConfig.setName(name);
			
			String label = XMLParseUtil.getAttribute(fieldNode, "label");
			if (StringUtil.isEmpty(label)) {
				fieldConfig.setLabel(name);
			} else {
				fieldConfig.setLabel(label);
			}


            String multipleValueString = XMLParseUtil.getAttribute(fieldNode, "multipleValue");
            String multipleValueSeparator = XMLParseUtil.getAttribute(fieldNode, "multipleValueSeparator");
            String multipleValueType = XMLParseUtil.getAttribute(fieldNode, "multipleValueType");

            if (!StringUtil.isEmpty(multipleValueString)) {
                 fieldConfig.setMultipleValue(Boolean.valueOf(multipleValueString));
            }

            if (!StringUtil.isEmpty(multipleValueSeparator)) {
                fieldConfig.setMultipleValueSeparator(multipleValueSeparator);
            }

            if (!StringUtil.isEmpty(multipleValueType)) {
                fieldConfig.setMultipleValueType(multipleValueType);
            }

			
			NodeList validators = (NodeList)formFieldValidatorExpr.evaluate(fieldNode, XPathConstants.NODESET);
			evalValidators(validators, fieldConfig);
			formConfig.getFieldConfigs().put(name, fieldConfig);
		}
	}
	
	private void evalValidators(NodeList validators, FieldConfig fieldConfig) throws XPathExpressionException {
		if (validators == null) {
			return;
		}
		for (int i = 0, len = validators.getLength(); i < len; ++i) {
			Node validatorNode = validators.item(i);
			String type = XMLParseUtil.getAttribute(validatorNode, "type");
			if (StringUtil.isEmpty(type)) {
				throw new FormConfigException("form[\""+ fieldConfig.getFormConfig().getName() +"\"]\'s field[\""+ fieldConfig.getName() +"\"] has a validator whose type is empty!");
			}
			String message = XMLParseUtil.getAttribute(validatorNode, "message");
			if (StringUtil.isEmpty(message)) {
				throw new FormConfigException("form[\""+ fieldConfig.getFormConfig().getName() +"\"]\'s field[\""+ fieldConfig.getName() +"\"]\'s validator[\""+ type +"\"] whose message empty!");
			}
			ValidatorConfig validatorConfig = new ValidatorConfig();
			validatorConfig.setType(type);
			validatorConfig.setFieldConfig(fieldConfig);
			validatorConfig.setRawMessage(message);
			
			NodeList paramNodes = (NodeList)formFieldValidatorParamExpr.evaluate(validatorNode, XPathConstants.NODESET);
			MagicMap parameters = getValidatorParams(paramNodes, validatorConfig);
			validatorConfig.setParameters(parameters);
			validatorConfig.init(simpleTemplateEngine, i18nResourceFinder);
			
			fieldConfig.getValidatorConfigs().add(validatorConfig);
		}
	}
	
	private MagicMap getValidatorParams(NodeList params, ValidatorConfig validatorConfig) {
		if (params == null) {
			return MagicMap.newMagicMap();
		}
		MagicMap ret = MagicMap.newMagicMap();
		for (int i = 0, len = params.getLength(); i < len; ++i) {
			Node paramNode = params.item(i);
			String name = XMLParseUtil.getAttribute(paramNode, "name");
			if (StringUtil.isEmpty(name)) {
				throw new FormConfigException("form[\""+ validatorConfig.getFieldConfig().getFormConfig().getName() +"\"]\'s field[\""+ 
					validatorConfig.getFieldConfig().getName() +"\"]\'s validator[\""+ validatorConfig.getType() +"\"] whose param name is empty!");
			}
			String value = XMLParseUtil.getAttribute(paramNode, "value");
			if (StringUtil.isEmpty(value)) {
				throw new FormConfigException("form[\""+ validatorConfig.getFieldConfig().getFormConfig().getName() +"\"]\'s field[\""+ 
					validatorConfig.getFieldConfig().getName() +"\"]\'s validator[\""+ validatorConfig.getType() +"\"] whose param value is empty!");
			}
			ret.put(name, value);
		}
		return ret;
	}
	
	private void evalResources(NodeList resources, Map<String, FormConfig> formConfigs, Set<String> resourceNames) {
		if (resources == null) {
			return;
		}
		for (int i = 0, len = resources.getLength(); i < len; ++i) {
			Node resourceNode = resources.item(i);
			String file = XMLParseUtil.getAttribute(resourceNode, "file");
			if (StringUtil.isEmpty(file)) {
				throw new FormConfigException("resource\'s attribute file can not be empty!");
			}
			if (resourceNames.contains(file)) {
				throw new FormConfigException("resource \""+ file + "\" has duplicated");
			}
			this.parseImpl(file, formConfigs, resourceNames);
		}
	}

}
