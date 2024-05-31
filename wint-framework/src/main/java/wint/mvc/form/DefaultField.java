package wint.mvc.form;

import wint.help.biz.result.MessageRender;
import wint.lang.utils.StringUtil;
import wint.mvc.form.config.FieldConfig;

public class DefaultField implements Field {
	
	private FieldConfig fieldConfig;
	
	private Form form;
	
	private String value;
	
	private String[] values;
	
	private MessageRender message;

	public DefaultField(FieldConfig fieldConfig, Form form) {
		super();
		this.fieldConfig = fieldConfig;
		this.form = form;
	}

	public String getName() {
		return fieldConfig.getName();
	}

	public String getLabel() {
		return fieldConfig.getLabel();
	}
	
	public FieldConfig getFieldConfig() {
		return fieldConfig;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MessageRender getMessage() {
		return message;
	}

	public void setMessage(MessageRender message) {
		this.message = message;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

    public int getValuesLength() {
        if (value == null || values == null) {
            return 0;
        }
        int count = 0;
        for (String v : values) {
            if (!StringUtil.isEmpty(v)) {
                 count++;
            }
        }
        return count;
    }

    public boolean hasValue(Object value)  {
        if (value == null) {
            return false;
        }
        if (values == null || values.length == 0) {
            return false;
        }
        String stringValue = value.toString();
        for (String v : values) {
            if (StringUtil.equals(v, stringValue)) {
                return true;
            }
        }
        return false;
    }

}
