package wint.mvc.form;

import wint.mvc.form.config.FieldConfig;

public interface Field {

	String getName();

	String getLabel();

	void setValue(String value);
	
	String getValue();

	String[] getValues();

	void setValues(String[] values);
	
	String getMessage();

	void setMessage(String message);
	
	FieldConfig getFieldConfig();

}
