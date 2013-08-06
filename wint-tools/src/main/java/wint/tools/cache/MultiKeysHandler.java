package wint.tools.cache;

import java.util.Collection;
import java.util.Map;

public interface MultiKeysHandler<K, V> {
	
	Map<K, V> fetchMultiItems(Collection<K> keys) throws Exception;

}
