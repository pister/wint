package wint.tools.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import wint.tools.redis.RedisClient;
import wint.tools.util.CollectionUtil;

/**
 * 简化cache使用模型
 * 2012-6-4 下午12:05:34
 */
public class RedisCacheManager extends AbstractCacheManager implements CacheManager {
	
	private RedisClient redisClient;

    public RedisCacheManager(RedisClient redisClient) {
		super();
		this.redisClient = redisClient;
	}

    public RedisCacheManager() {
		super();
	}
	
	public <K, V> void mset(int namespace, Map<K, V> m, int expireInSeconds) {
		// 在没有更好的方式设置批量过期前，目前只有两种方式：
		// 一是先批量设置，然后在逐个设置过期时间
		// 二是逐个带过期时间的设置
		// 这里采用前者
		try {
			if (CollectionUtil.isEmpty(m)) {
				return;
			}
			Map<K, Entry<V>> em = archive(m);
			redisClient.mset(namespace, em);
            for (Map.Entry<K, V> entry : m.entrySet()) {
                redisClient.expire(namespace, entry.getKey(), expireInSeconds);
            }
		} catch (Exception e) {
			logger.error("cache error", e);
		}
	}
	
	public <K, V> void set(int namespace, K key, V value, int expireInSeconds) {
		try {
			redisClient.set(namespace, key, new Entry<V>(value));
            redisClient.expire(namespace, key, expireInSeconds);
		} catch (Exception e) {
			logger.error("cache error", e);
		}
	}
	
	public <K, V> Map<K, Entry<V>> mget(int namespace, Collection<K> keys) {
		try {
			List<Entry<V>> values = redisClient.mget(namespace, keys);
			if (CollectionUtil.isEmpty(keys) || CollectionUtil.isEmpty(values)) {
				return CollectionUtil.newHashMap();
			}
			/*
			 是按照key的顺序返回的
			 */
			Map<K, Entry<V>> ret = CollectionUtil.newHashMap();
			
			Iterator<K> keyIt = keys.iterator();
			Iterator<Entry<V>> entryIt = values.iterator();
			
			while (keyIt.hasNext() && entryIt.hasNext()) {
				K k = keyIt.next();
				Entry<V> e = entryIt.next();
				ret.put(k, e);
			}
			return ret;		
		} catch (Exception e) {
			logger.error("cache error", e);
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public <K, V> Entry<V> get(int namespace, K key) {
		if (!enable) {
			return null;
		}
		try {
			return (Entry<V>)redisClient.get(namespace, key);
		} catch (Exception e) {
			logger.error("cache error", e);
			return null;
		}
	}
	
	private <K, V> Map<K, Entry<V>> archive(Map<K, V> m) {
		Map<K, Entry<V>> ret = CollectionUtil.newHashMap();
		for (Map.Entry<K, V> entry : m.entrySet()) {
			ret.put(entry.getKey(), new Entry<V>(entry.getValue()));
		}
		return ret;
	}
	

	public <K> void delete(int namespace, K... keys) {
		if (!enable) {
			return;
		}
		redisClient.delete(namespace, keys);
	}

	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}

}
