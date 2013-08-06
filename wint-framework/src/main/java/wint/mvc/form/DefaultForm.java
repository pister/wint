package wint.mvc.form;

import java.util.List;
import java.util.Map;

import wint.core.config.Constants;
import wint.lang.magic.MagicList;
import wint.lang.magic.MagicObject;
import wint.lang.magic.Property;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.config.FieldConfig;
import wint.mvc.form.config.FormConfig;
import wint.mvc.form.config.ValidatorConfig;
import wint.mvc.form.runtime.FormFactory;
import wint.mvc.form.runtime.ResultRunTimeForm;
import wint.mvc.form.validator.ValidateResult;
import wint.mvc.form.validator.Validator;
import wint.mvc.parameters.Parameters;

/**
 * @author pister
 * 2012-2-12 01:29:11
 */
public class DefaultForm implements Form {

	private FormConfig formConfig;
	
	private InnerFlowData flowData;
	
	private ValidateResult validateResult;
	
	private Map<String, Field> fields;
	
	private boolean isHeld = false;

	public DefaultForm(FormConfig formConfig, InnerFlowData flowData) {
		super();
		this.formConfig = formConfig;
		this.flowData = flowData;
		this.fields = MapUtil.newHashMap();
		initFields();
	}

	private void initFields() {
		 Map<String, FieldConfig> fieldConfigs = formConfig.getFieldConfigs();
		 for (Map.Entry<String, FieldConfig> entry : fieldConfigs.entrySet()) {
			 FieldConfig fieldConfig = entry.getValue();
			 DefaultField defaultField = new DefaultField(fieldConfig, this);
			 fields.put(fieldConfig.getName(), defaultField);
		 }
	}
	
	public void hold(Object object) {
		if (isHeld) {
			return;
		}
		ResultRunTimeForm resultFormFactory = new ResultRunTimeForm(this, object);
		FormFactory formFactory = (FormFactory)flowData.getInnerContext().get(Constants.Form.TEMPLATE_FORM_FACTORY_NAME);
		formFactory.addResultForm(getName(), resultFormFactory);
		isHeld = true;
	}
	
	public boolean apply(Object target) {
		if (validateResult == null) {
			validate();
		}
		if (!validateResult.isSuccess()) {
			return false;
		}
		MagicObject magicObject = MagicObject.wrap(target);
		Map<String, Property> properties = magicObject.getMagicClass().getProperties();
		
		for (Map.Entry<String, Field> entry: fields.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue().getValue();
			// TODO array or list, how ?
		//	String[] values = entry.getValue().getValues();
			Property property = properties.get(name);
			if (property == null) {
				continue;
			}
			if (!property.isWritable()) {
				continue;
			}
			property.setValueExt(target, value);
		}
		return true;
	}
	
	public String getName() {
		return formConfig.getName();
	}

	public boolean validate() {
		if (validateResult != null) {
			return validateResult.isSuccess();
		}
		ValidateResult theResult = new ValidateResult();
		Parameters parameters = flowData.getParameters();
		Map<String, FieldConfig> fieldConfigs = formConfig.getFieldConfigs();
		for (Map.Entry<String, FieldConfig> entry : fieldConfigs.entrySet()) { 
			String fieldName = entry.getKey();
			FieldConfig fieldConfig = entry.getValue();
			MagicList<ValidatorConfig> validatorConfigs = fieldConfig.getValidatorConfigs();
			List<String> messages = CollectionUtil.newArrayList();
			theResult.setFieldMessages(fieldName, messages);
			String fieldValue = parameters.getString(fieldName);
			Field field = fields.get(fieldName);
			field.setValue(fieldValue);
			String[] values = parameters.getStringArray(fieldName);
			field.setValues(values);
			for (ValidatorConfig validatorConfig : validatorConfigs) {
				Validator validator = validatorConfig.getValidator();
				boolean result = validator.validate(flowData, formConfig, fieldName, fieldValue);
				if (!result) {
					field.setMessage(validatorConfig.getMessage());
					messages.add(validatorConfig.getMessage());
					break;
				}
			}
		}
		validateResult = theResult;
		boolean ret = validateResult.isSuccess();
		holdRequest();
		return ret;
	}
	
	public void holdRequest() {
		ResultRunTimeForm resultFormFactory = new ResultRunTimeForm(this);
		FormFactory formFactory = (FormFactory)flowData.getInnerContext().get(Constants.Form.TEMPLATE_FORM_FACTORY_NAME);
		formFactory.addResultForm(getName(), resultFormFactory);
		isHeld = true;
	}
	
	public void clearHold() {
		FormFactory formFactory = (FormFactory)flowData.getInnerContext().get(Constants.Form.TEMPLATE_FORM_FACTORY_NAME);
		formFactory.removeResultForm(getName());
		isHeld = false;
	}

	public ValidateResult getValidateResult() {
		return validateResult;
	}

	public Map<String, Field> getFields() {
		return fields;
	}

	public void setFields(Map<String, Field> fields) {
		this.fields = fields;
	}

	public boolean isHeld() {
		return isHeld;
	}

}
