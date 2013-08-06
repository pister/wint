package wint.tools.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisClient {
	
	/**
	 * 用于获取计数器的值
	 * @param namespace
	 * @param key
	 * @return
	 */
	<K> long getCounting(final int namespace, final K key);
	
	/**
	 * 于获取计数器的值
	 * @param namespace
	 * @param key
	 * @param hashKey
	 * @return
	 */
	<K, HashKey> long hgetCounting(int namespace, K key, HashKey hashKey);
	
	<K, V> void mset(int namespace, Map<K, V> m);
	
	<K, V> void set(int namespace, K key, V value);
	
	<K, V> List<V> mget(int namespace, Collection<K> keys);
	
	<K, V> V get(int namespace, K key);
	
	<K> void delete(int namespace, K... keys);
	
	<K, V> V getSet(int namespace, K key, V newValue);
	
	<K> void expire(int namespace, K key, int seconds);

	<K> Long decrBy(int namespace, K key, long integer);
	
	<K> Long decr(int namespace, K key);
	
	<K> Long incrBy(int namespace, K key, long integer);
	
	<K> Long incr(int namespace, K key);
	
	<K> Boolean exists(int namespace, K key);
	
	<K, V> Long append(int namespace, K key, V value);
	
	<K, HashKey, HashValue> Long hset(int namespace, K key, HashKey hashKey, HashValue hashValue);
	
	<K, HashKey, HashValue> HashValue hget(int namespace, K key, HashKey hashKey);
	
	<K, HashKey, HashValue> String hmset(int namespace, K key, Map<HashKey, HashValue> hash);
	
	<K, HashKey, HashValue> List<HashValue> hmget(int namespace, K key, HashKey... hashKeys);
	
	<K, HashKey> Long hincrBy(int namespace, K key, HashKey hashKey, long value);
	
	<K, HashKey> Boolean hexists(int namespace, K key, HashKey hashKey);
	
	<K, HashKey> Long hdel(int namespace, K key, HashKey... hashKeys);
	
	<K, HashKey> Long hlen(int namespace, K key);
	
	<K, HashKey> Set<HashKey> hkeys(int namespace, K key);
	
	<K, HashValue> List<HashValue> hvals(int namespace, K key);
	
	<K, HashKey, HashValue> Map<HashKey, HashValue> hgetAll(int namespace, K key);
	
	<K, V> Long rpush(int namespace, K key, V... strings);
	
	<K, V> Long lpush(int namespace, K key, V... strings);
	
	<K> Long llen(int namespace, K key);
	
	<K, V> List<V> lrange(int namespace, K key, int start, int end);
	
	<K, V> V lindex(int namespace, K key, int index);
	
	<K, V> String lset(int namespace, K key, int index, V value);
	
	<K, V> Long lrem(int namespace, K key, int count, V value);
	
	<K, V> V lpop(int namespace, K key);
	
	<K, V> V rpop(int namespace, K key);
	
	<K, V> Long sadd(int namespace, K key, V... members);
	
	<K, V> Set<V> smembers(int namespace, K key);
	
	<K, V> Long srem(int namespace, K key, V... members);
	
	<K> Long scard(int namespace, K key);
	
	<K, V> Boolean sismember(int namespace, K key, V member);
	
	void close();
	
}
