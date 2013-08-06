package wint.tools.cache;

import java.util.Collection;
import java.util.Map;

/**
 * @author longyi.hsl
 * 2012-6-4 下午12:05:20
 */
public interface CacheManager {

	<K, V> V getForHandle(int namespace, K key, SingleKeyHandler<K, V> cacheHandler, int expireInSeconds);

	<K, V> Map<K, V> mgetForHandle(int namespace, Collection<K> keys, MultiKeysHandler<K, V> cacheHandler, int expireInSeconds);
	
	<K> void delete(int namespace, K... keys);

	<K, V> Entry<V> get(int namespace, K key);
	
	<K, V> Map<K, Entry<V>> mget(int namespace, Collection<K> keys);
	
	<K, V> void mset(int namespace, Map<K, V> m, int expireInSeconds);
	
	<K, V> void set(int namespace, K key, V value, int expireInSeconds);
	
	boolean isEnable();
	
	void setEnable(boolean enable);
	
}
