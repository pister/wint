package wint.mvc.form;

import java.util.Map;

import wint.core.config.Constants;
import wint.core.io.resource.Resource;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.AbstractService;
import wint.lang.exceptions.CanNotFindFormException;
import wint.lang.template.SimpleTemplateEngine;
import wint.lang.template.SimpleVelocityEngine;
import wint.lang.utils.MapUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.config.FormConfig;
import wint.mvc.form.config.FormConfigLoader;
import wint.mvc.form.config.XmlFormConfigLoader;

public class DefaultFormService extends AbstractService implements FormService {

	private Map<String, FormConfig> formConfigs;
	
	private SimpleTemplateEngine simpleTemplateEngine;
	
	@Override
	public void init() {
		super.init();
		ResourceLoader resourceLoader = serviceContext.getResourceLoader();
		Resource defaultFormResource = resourceLoader.getResource(Constants.Form.FORM_CONFIG_FILE);
		if (defaultFormResource != null && defaultFormResource.exist()) {
			SimpleVelocityEngine simpleVelocityEngine  = new SimpleVelocityEngine();
			String encoding = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.CHARSET_ENCODING, Constants.Defaults.CHARSET_ENCODING);
			simpleVelocityEngine.init(encoding);
			this.simpleTemplateEngine = simpleVelocityEngine;
			
			if (log.isInfoEnabled()) {
				log.info("form config file \""+ Constants.Form.FORM_CONFIG_FILE +"\" has exist, loading it...");
			}
			FormConfigLoader formConfigLoader = new XmlFormConfigLoader(serviceContext.getResourceLoader(),
					simpleVelocityEngine, serviceContext.getConfiguration());
			formConfigs = formConfigLoader.parse(Constants.Form.FORM_CONFIG_FILE).getFormConfigs();
			if (log.isInfoEnabled()) {
				log.info("loading form config success.");
				log.info("the form name\'s are " + formConfigs.keySet());
			}
			if (log.isDebugEnabled()) {
				log.debug(getFormConfigsInfo());
			}
		} else {
			formConfigs = MapUtil.newHashMap();
			if (log.isInfoEnabled()) {
				log.info("form config file \""+ Constants.Form.FORM_CONFIG_FILE +"\" not exist.");
			}
		}
	}
	
	private String getFormConfigsInfo() {
		String formConfigTemplate = "form: $formConfig.name\n" +
									"#foreach($fieldConfig in $formConfig.fieldConfigs.values())" +
										"\tfield: name: $fieldConfig.name, label: $!fieldConfig.label\n" +
										"#foreach($validatorConfig in $fieldConfig.validatorConfigs)" +
											"\t\tvalidator: type: $validatorConfig.validator, message: $validatorConfig.message\n" +
										"#end" +
									"#end";
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, FormConfig> entry : formConfigs.entrySet()) {
			Map<String, Object> context = MapUtil.newHashMap();
			context.put("formConfig", entry.getValue());
			String formInfo = simpleTemplateEngine.merge(formConfigTemplate, context);
			sb.append(formInfo);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public Form getForm(String name, InnerFlowData flowData) {
		FormConfig formConfig = formConfigs.get(name);
		if (formConfig == null) {
			throw new CanNotFindFormException("can not find form for name: " + name + ", Are you import the config to form.xml? ");
		}
		return new DefaultForm(formConfig, flowData);
	}
	
}
