package wint.tools.cache;

/**
 * @author longyi.hsl
 * 2012-6-4 下午1:47:37
 * @param <K>
 * @param <T>
 */
public interface SingleKeyHandler<K, T> {
	
	T fetchItem(K key) throws Exception;

}
