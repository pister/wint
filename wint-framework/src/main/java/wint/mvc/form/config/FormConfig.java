package wint.mvc.form.config;

import java.util.Map;

import wint.lang.utils.MapUtil;

public class FormConfig {
	
	private String name;
	
	private String extendsFormName;
	
	private Map<String, FieldConfig> fieldConfigs = MapUtil.newHashMap();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, FieldConfig> getFieldConfigs() {
		return fieldConfigs;
	}

	public void setFieldConfigs(Map<String, FieldConfig> fieldConfigs) {
		this.fieldConfigs = fieldConfigs;
	}

	public String getExtendsFormName() {
		return extendsFormName;
	}

	public void setExtendsFormName(String extendsFormName) {
		this.extendsFormName = extendsFormName;
	}
	
}
