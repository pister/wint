package wint.sessionx.store;

import java.io.Serializable;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:33
 */
public class SessionData implements Serializable {

    private static final long serialVersionUID = 6823163590591332462L;

    private String name;

    private Object data;

    public SessionData(String name, Object data) {
        this.name = name;
        this.data = data;
    }

    public SessionData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
