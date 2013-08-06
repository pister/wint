package wint.tools.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CollectionUtil {

	public static <K, V> HashMap<K, V> newHashMap(Map<K, V> m) {
		return new HashMap<K, V>(m);
	}
	
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}
	
	public static <K, V> boolean isEmpty(Map<K, V> m) {
		return null == m || m.isEmpty();
	}
	
	public static <E> boolean isEmpty(Collection<E> c) {
		return null == c || c.isEmpty();
	}
	
	public static <E> ArrayList<E> newArrayList() {
		return new ArrayList<E>();
	}
	
	public static <E> ArrayList<E> newArrayList(Collection<E> c) {
		return new ArrayList<E>(c);
	}
	
	
	public static <E> ArrayList<E> newArrayList(int size) {
		return new ArrayList<E>(size);
	}
	
}
