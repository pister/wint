package wint.mvc.form.config;

import wint.lang.magic.MagicList;

public class FieldConfig {
	
	private String name;
	
	private String label;

    private boolean multipleValue = false;

    private String multipleValueSeparator = ",";

    private String multipleValueType = "string";
	
	private FormConfig formConfig;
	
	private MagicList<ValidatorConfig> validatorConfigs = MagicList.newList();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public MagicList<ValidatorConfig> getValidatorConfigs() {
		return validatorConfigs;
	}

	public void setValidatorConfigs(MagicList<ValidatorConfig> validatorConfigs) {
		this.validatorConfigs = validatorConfigs;
	}

	public FormConfig getFormConfig() {
		return formConfig;
	}

	public void setFormConfig(FormConfig formConfig) {
		this.formConfig = formConfig;
	}

    public boolean isMultipleValue() {
        return multipleValue;
    }

    public void setMultipleValue(boolean multipleValue) {
        this.multipleValue = multipleValue;
    }

    public String getMultipleValueSeparator() {
        return multipleValueSeparator;
    }

    public void setMultipleValueSeparator(String multipleValueSeparator) {
        this.multipleValueSeparator = multipleValueSeparator;
    }

    public String getMultipleValueType() {
        return multipleValueType;
    }

    public void setMultipleValueType(String multipleValueType) {
        this.multipleValueType = multipleValueType;
    }
}
