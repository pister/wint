package wint.lang.magic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import wint.lang.convert.ConvertUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;

/**
 * map的方便包装类
 * @author pister
 */
public class MagicMap implements Map<String, Object> {

	private Map<String, Object> map;
	
	public MagicMap(Map<String, Object> map) {
		super();
		this.map = map;
	}

	public static MagicMap newMagicMap() {
		return new MagicMap(new HashMap<String, Object>());
	}
	
	public String getString(String name) {
		return getString(name, null);
	}

    public String getStringTrim(String name) {
        String s = getString(name, null);
        if (s == null) {
            return null;
        }
        return StringUtil.trimToEmpty(s);
    }


    public String join(String kvCat, String entryCat) {
		return join(kvCat, entryCat, null);
	}
	
	public String join(String kvCat, String entryCat, Transformer<Object, String> valueTransformer) {
		return MapUtil.join(this, kvCat, entryCat, valueTransformer);
	}
	
	public String getString(String name, String defaultValue) {
		if (!containsKey(name)) {
			return defaultValue;
		}
		Object v = get(name);
		if (v == null) {
			return defaultValue;
		}
		return v.toString();
	}
	
	public boolean getBoolean(String name) {
		return getBoolean(name, false);
	}
	
	public boolean getBoolean(String name, boolean defaultValue) {
		return ConvertUtil.toBoolean(getString(name), defaultValue);
	}
	
	public long getLong(String name) {
		return getLong(name, 0L);
	}
	
	public long getLong(String name, long defaultValue) {
		return ConvertUtil.toLong(getString(name), defaultValue);
	}
	
	public int getInt(String name, int defaultValue) {
		return ConvertUtil.toInt(getString(name), defaultValue);
	}
	
	public int getInt(String name) {
		return getInt(name, 0);
	}
	
	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	public boolean equals(Object o) {
		return map.equals(o);
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public int hashCode() {
		return map.hashCode();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		map.putAll(m);
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public String toString() {
		return map.toString();
	}

}
