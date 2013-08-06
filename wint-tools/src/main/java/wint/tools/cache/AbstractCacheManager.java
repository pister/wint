package wint.tools.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.tools.util.CollectionUtil;

public abstract class AbstractCacheManager implements CacheManager {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected boolean enable = true;
	
	private <K, V> Map<K, V> extract(Map<K, Entry<V>> m) {
		Map<K, V> ret = CollectionUtil.newHashMap();
		for (Map.Entry<K, Entry<V>> entry : m.entrySet()) {
			Entry<V> e = entry.getValue();
			if (e != null) {
				ret.put(entry.getKey(), e.getObject());
			}
		}
		return ret;
	}

	public <K, V> Map<K, V> mgetForHandle(int namespace, Collection<K> keys, final MultiKeysHandler<K, V> cacheHandler, int expireInSeconds) {
		if (!enable) {
			try {
				return cacheHandler.fetchMultiItems(keys);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		Map<K, Entry<V>> values = mget(namespace, keys);
        if (values == null) {
            values = CollectionUtil.newHashMap();
        }
		
		List<K> noDataKeys;
		
		if (CollectionUtil.isEmpty(values)) {
			noDataKeys = CollectionUtil.newArrayList(keys);
		} else {
			noDataKeys = CollectionUtil.newArrayList();
			for (Map.Entry<K, Entry<V>> entry : values.entrySet()) {
				K k = entry.getKey();
				Entry<V> e = entry.getValue();
				if (e == null) {
					noDataKeys.add(k);
				}
			}
		}
		
		if (CollectionUtil.isEmpty(noDataKeys)) {
			return extract(values);
		}
		
		Map<K, V> newItems;
		try {
			newItems = cacheHandler.fetchMultiItems(noDataKeys);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		// cacheHandler.fetchMultiItems([1,2,3])的返回结果可能是
		// {1:xx, 2:xxx, 3: xxx}， 也可能是 {2:xxx}
		// 如果每次只是返回有值的k-v，那么其他key每次还是需要去取一次，没有命中缓存
		newItems = addNullValueForNoData(newItems, noDataKeys);
		
		mset(namespace, newItems, expireInSeconds);
		
		Map<K, V> ret = CollectionUtil.newHashMap(extract(values));
		ret.putAll(newItems);
		return ret;
	}
	
	private <K, V> Map<K, V> addNullValueForNoData(Map<K, V> newItems, List<K> noDataKeys) {
		if (newItems == null) {
			newItems = CollectionUtil.newHashMap();
		}
		for (K k : noDataKeys) {
			if (!newItems.containsKey(k)) {
				newItems.put(k, null);
			}
		}
		return newItems;
	}

	@SuppressWarnings("unchecked")
	public <K, T> T getForHandle(int namespace, K key, SingleKeyHandler<K, T> cacheHandler, int expireInSeconds) {
		if (!enable) {
			try {
				return cacheHandler.fetchItem(key);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		Entry<T> ev = (Entry<T>)get(namespace, key);
		if (ev != null) {
			return ev.getObject();
		}
		T value;
		try {
			value = cacheHandler.fetchItem(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		set(namespace, key, value, expireInSeconds);
		return value;
	}

	public boolean isEnable() {
		return false;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
