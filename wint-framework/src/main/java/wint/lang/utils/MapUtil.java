package wint.lang.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import wint.lang.magic.Transformer;

public class MapUtil {

	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}
	
	public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> m) {
		return new HashMap<K, V>(m);
	}
	
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<K, V>();
	}
	
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(Map<? extends K, ? extends V> m) {
		return new ConcurrentHashMap<K, V>(m);
	}
	
	public static <K, V> Map<K, V> newUnmodifiableMap() {
		return Collections.unmodifiableMap(new HashMap<K, V>());
	}
	
	public static <K, V> boolean isEmpty(Map<K, V> m) {
		if (m == null || m.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static <K, V> String join(Map<K, V> map, String kvCat, String entryCat) {
		return join(map, kvCat, entryCat, null);
	}
	
	public static <K, V> String join(Map<K, V> map, String kvCat, String entryCat, Transformer<V, String> valueTransformer) {
		if (isEmpty(map)) {
			return StringUtil.EMPTY;
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (first) {
				first = false;
			} else {
				sb.append(entryCat);
			}
			sb.append(entry.getKey());
			sb.append(kvCat);
			if (valueTransformer != null) {
				Object o = valueTransformer.transform(entry.getValue());
				sb.append(o);
			} else {
				sb.append(entry.getValue());
			}
		}
		return sb.toString();
	}
	

	
}
