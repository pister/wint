package wint.mvc.flow;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

/**
 * User: longyi
 * Date: 13-9-3
 * Time: 上午9:50
 */
public class ServletHttpSession implements Session {

    private HttpSession httpSession;

    public ServletHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public void setAttribute(String name, Object value) {
        httpSession.setAttribute(name, value);
    }

    public Object getAttribute(String name) {
        return httpSession.getAttribute(name);
    }

    public void removeAttribute(String name) {
        httpSession.removeAttribute(name);
    }

    public String getId() {
        return httpSession.getId();
    }

    public List<String> getAttributeNames() {
        return (List<String>)Collections.list(httpSession.getAttributeNames());
    }
}
