package wint.lang.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author pister 2012-3-6 09:44:25
 * @param <K>
 * @param <V>
 */
public class CopyOnWriteHashMap<K, V> implements Map<K, V> {

	transient final ReentrantLock lock = new ReentrantLock();

    private volatile transient HashMap<K, V> hashMap;
	
    final HashMap<K, V> getHashMap() {
        return hashMap;
    }

    final void setHashMap(HashMap<K, V> m) {
    	hashMap = m;
    }
    
	public void clear() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			hashMap.clear();
		} finally {
		    lock.unlock();
		}
	}
	
	public CopyOnWriteHashMap() {
		setHashMap(new HashMap<K, V>());
	}
	
	public CopyOnWriteHashMap(Map<? extends K, ? extends V> m) {
		setHashMap(new HashMap<K, V>(m));
	}

	public boolean containsKey(Object key) {
		return hashMap.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return hashMap.containsValue(value);
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return Collections.unmodifiableSet(hashMap.entrySet());
	}

	public V get(Object key) {
		return hashMap.get(key);
	}

	public boolean isEmpty() {
		return hashMap.isEmpty();
	}

	public Set<K> keySet() {
		return Collections.unmodifiableSet(hashMap.keySet());
	}

	public V put(K key, V value) {
		final ReentrantLock lock = this.lock;
		V v;
		lock.lock();
		try {
			HashMap<K, V> newHashMap = new HashMap<K, V>(getHashMap());
			v = newHashMap.put(key, value);
			this.setHashMap(newHashMap);
		} finally {
		    lock.unlock();
		}
		return v;
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			HashMap<K, V> newHashMap = new HashMap<K, V>(getHashMap());
			newHashMap.putAll(m);
			this.setHashMap(newHashMap);
		} finally {
		    lock.unlock();
		}
	}

	public V remove(Object key) {
		final ReentrantLock lock = this.lock;
		V v;
		lock.lock();
		try {
			HashMap<K, V> newHashMap = new HashMap<K, V>(getHashMap());
			v = newHashMap.remove(key);
			this.setHashMap(newHashMap);
		} finally {
		    lock.unlock();
		}
		return v;
	}

	public int size() {
		return hashMap.size();
	}

	public Collection<V> values() {
		return Collections.unmodifiableCollection(hashMap.values());
	}

	@Override
	public String toString() {
		return hashMap.toString();
	}

}
