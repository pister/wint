package wint.mvc.form.config;

import java.util.Map;

import wint.core.config.Constants;
import wint.lang.magic.MagicMap;
import wint.lang.template.SimpleTemplateEngine;
import wint.lang.utils.MapUtil;
import wint.mvc.form.validator.Validator;
import wint.mvc.form.validator.ValidatorUtil;

public class ValidatorConfig {

	private String type;
	
	private MagicMap parameters;
	
	private String rawMessage;
	
	private String message;
	
	private Validator validator;
	
	private FieldConfig fieldConfig;
	
	public void init(SimpleTemplateEngine simpleTemplateEngine) {
		evalValidatorObject();
		evalMessage(simpleTemplateEngine);
	}
	
	private void evalValidatorObject() {
		validator = ValidatorUtil.lookFor(type, parameters);
	}
	
	private void evalMessage(SimpleTemplateEngine simpleTemplateEngine) {
		Map<String, Object> context = MapUtil.newHashMap();
		context.put(Constants.Form.VAR_NAME_FORM, fieldConfig.getFormConfig());
		context.put(Constants.Form.VAR_NAME_FIELD, fieldConfig);
		if (!MapUtil.isEmpty(parameters)) {
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}
		}
		message = simpleTemplateEngine.merge(rawMessage, context);
	}
	
	public Validator getValidator() {
		return validator;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MagicMap getParameters() {
		return parameters;
	}

	public void setParameters(MagicMap parameters) {
		this.parameters = parameters;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FieldConfig getFieldConfig() {
		return fieldConfig;
	}

	public void setFieldConfig(FieldConfig fieldConfig) {
		this.fieldConfig = fieldConfig;
	}

	public String getRawMessage() {
		return rawMessage;
	}

	public void setRawMessage(String rawMessage) {
		this.rawMessage = rawMessage;
	}

}
