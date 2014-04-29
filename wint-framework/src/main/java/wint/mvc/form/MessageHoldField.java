package wint.mvc.form;

import wint.mvc.form.config.FieldConfig;

/**
 * User: huangsongli
 * Date: 14-4-24
 * Time: 下午3:46
 */
public class MessageHoldField implements Field {

    private String name;

    private String message;

    public MessageHoldField(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        throw new UnsupportedOperationException();
    }

    public void setValue(String value) {
        throw new UnsupportedOperationException();
    }

    public String getValue() {
        throw new UnsupportedOperationException();
    }

    public String[] getValues() {
        throw new UnsupportedOperationException();
    }

    public void setValues(String[] values) {
        throw new UnsupportedOperationException();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FieldConfig getFieldConfig() {
        throw new UnsupportedOperationException();
    }

    public boolean hasValue(Object value) {
        return false;
    }

    public int getValuesLength() {
        return 0;
    }
}
