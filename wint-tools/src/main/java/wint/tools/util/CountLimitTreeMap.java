package wint.tools.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;



/**
 * 大小有上限的map， 线程不安全！
 * @author longyi.hsl
 * 2012-8-21 下午3:58:14
 * @param <K>
 * @param <V>
 */
public class CountLimitTreeMap<K, V> implements Serializable, Map<K, V> {

	private static final long serialVersionUID = 4175939831414902683L;
	
	private int maxSize;
	
	private TreeMap<K, V> map = new TreeMap<K, V>();
	
	public CountLimitTreeMap(int maxSize) {
		super();
		this.maxSize = maxSize;
	}

	@Override
	public String toString() {
		return map.toString();
	}

	protected void ensureSize() {
		while (map.size() > maxSize ) {
			map.remove(map.firstKey());
		}
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

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	public boolean equals(Object o) {
		return map.equals(o);
	}

	public V get(Object key) {
		return map.get(key);
	}

	public int hashCode() {
		return map.hashCode();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public V put(K key, V value) {
		V v = map.put(key, value);
		ensureSize();
		return v;
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
		ensureSize();
	}

	public V remove(Object key) {
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public Collection<V> values() {
		return map.values();
	}
	

}
