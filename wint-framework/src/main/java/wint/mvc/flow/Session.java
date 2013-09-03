package wint.mvc.flow;

import java.util.List;

/**
 * User: longyi
 * Date: 13-9-3
 * Time: 上午9:49
 */
public interface Session {

    void setAttribute(String name, Object value);

    Object getAttribute(String name);

    void removeAttribute(String name);

    String getId();

    List<String> getAttributeNames();
}
