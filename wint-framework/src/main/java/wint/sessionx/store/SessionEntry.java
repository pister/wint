package wint.sessionx.store;

import java.io.Serializable;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:33
 */
public class SessionEntry implements Serializable{

    private static final long serialVersionUID = 6823163590591332462L;

    private String name;

    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
