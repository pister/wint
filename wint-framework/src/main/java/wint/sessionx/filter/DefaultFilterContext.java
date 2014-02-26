package wint.sessionx.filter;

import wint.lang.utils.MapUtil;

import java.util.Iterator;
import java.util.Map;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:22
 */
public class DefaultFilterContext implements FilterContext {

    private Iterator<Filter> filterIterator;

    private Map<String, Object> attributes = MapUtil.newHashMap();


    public DefaultFilterContext(Iterator<Filter> filterIterator) {
        this.filterIterator = filterIterator;
    }

    public void invokeNext() {
        if (filterIterator.hasNext()) {
            filterIterator.next().doFilter(this);
        }
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
