package wint.mvc.form;

import wint.help.biz.result.MessageRender;
import wint.mvc.form.config.FieldConfig;

public interface Field {

	String getName();

	String getLabel();

	void setValue(String value);
	
	String getValue();

	String[] getValues();

	void setValues(String[] values);

	MessageRender getMessage();

	void setMessage(MessageRender message);
	
	FieldConfig getFieldConfig();

    boolean hasValue(Object value);

    int getValuesLength();
}
