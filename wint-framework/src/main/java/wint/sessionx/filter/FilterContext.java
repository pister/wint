package wint.sessionx.filter;

import java.util.Map;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:19
 */
public interface FilterContext {

    void invokeNext();

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    Map<String, Object> getAttributes();
}