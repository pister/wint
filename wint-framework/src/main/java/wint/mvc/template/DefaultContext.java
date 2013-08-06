package wint.mvc.template;

import java.util.Map;

import wint.lang.utils.MapUtil;

/**
 * @author pister 2012-1-2 12:09:41
 */
public class DefaultContext implements Context {
	
	private Map<String, Object> values;
	
	public DefaultContext(Context context) {
		this(context.getAll());
	}
	
	public DefaultContext() {
		super();
		values = MapUtil.newHashMap();
	}

	public DefaultContext(Map<String, Object> values) {
		this();
		putAll(values);
	}

	public void put(String name, Object value) {
		values.put(name, value);
	}
	
	public void putAll(Map<String, Object> map) {
		values.putAll(map);
	}
	
	public Object get(String name) {
		return values.get(name);
	}
	
	public Map<String, Object> getAll() {
		return values;
	}

	public void putContext(Context context) {
		putAll(context.getAll());
	}

	public Context copyMe() {
		return new DefaultContext(this);
	}
	
	
}
