package wint.mvc.form.config;

import wint.lang.magic.MagicList;

public class FieldConfig {
	
	private String name;
	
	private String label;
	
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

}
