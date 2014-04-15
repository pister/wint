package wint.mvc.form;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import wint.core.config.Constants;
import wint.lang.convert.ConvertUtil;
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
import wint.mvc.parameters.MapParameters;
import wint.mvc.parameters.Parameters;
import wint.mvc.view.types.ViewTypes;

/**
 * @author pister
 * 2012-2-12 01:29:11
 */
public class DefaultForm implements Form {

	private FormConfig formConfig;
	
	private InnerFlowData flowData;
	
	private ValidateResult validateResult;
	
	private Map<String, Field> fields;

    private Parameters parameters;

	private boolean isHeld = false;

	public DefaultForm(FormConfig formConfig, InnerFlowData flowData) {
		super();
		this.formConfig = formConfig;
		this.flowData = flowData;
		this.fields = MapUtil.newHashMap();
		initFields();

        // 记录最后一次表单名称
        flowData.setAttribute(Constants.Form.LAST_FORM_NAME, getName());
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
        FormFactory formFactory = (FormFactory)flowData.getInnerContext().get(Constants.Form.TEMPLATE_FORM_FACTORY_NAME);
        if (formFactory.getResultForm(getName()) != null) {
            // 已经有form设置了，不再进行覆盖
            isHeld = true;
            return;
        }
        ResultRunTimeForm resultFormFactory = new ResultRunTimeForm(this, object);
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
            Field field = entry.getValue();
            FieldConfig fieldConfig = field.getFieldConfig();

			Property property = properties.get(name);
			if (property == null) {
				continue;
			}
			if (!property.isWritable()) {
				continue;
			}

            String value = entry.getValue().getValue();
            String[] values = entry.getValue().getValues();

            if (property.getPropertyClass().isArray()) {
                property.setValueExt(target, values);
                continue;
            }

            if (property.getPropertyClass().isCollectionLike()) {
                property.setValueExt(target, toTargetCollection(values, fieldConfig.getMultipleValueType()));
                continue;
            }

            if (fieldConfig.isMultipleValue()) {
                String stringValue = valuesToString(values, fieldConfig);
                property.setValueExt(target, stringValue);
                continue;
            }

			property.setValueExt(target, value);
		}
		return true;
	}

    protected String valuesToString(String[] values, FieldConfig fieldConfig) {
        return MagicList.wrap(values).join(fieldConfig.getMultipleValueSeparator());
    }

    protected Collection toTargetCollection(String[] values, String multipleValueType) {
        if (values == null) {
            return null;
        }
        Collection ret = CollectionUtil.newArrayList(values.length);
        for (String value : values) {
            Object newValue =  ConvertUtil.convertTo(value, multipleValueType);
            ret.add(newValue);
        }
        return ret;
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
        buildParameters();
		holdRequest();
		return ret;
	}

    private void buildParameters() {
        Map<String, String[]> values = MapUtil.newHashMap();
        for(Map.Entry<String, Field> entry : fields.entrySet()) {
            String name = entry.getKey();
            Field field = entry.getValue();
            if (field == null) {
                continue;
            }
            values.put(name, field.getValues());
        }
        parameters = new MapParameters(values);
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

    public Parameters getValues() {
        return parameters;
    }

    public void setFields(Map<String, Field> fields) {
		this.fields = fields;
	}

	public boolean isHeld() {
		return isHeld;
	}

}
