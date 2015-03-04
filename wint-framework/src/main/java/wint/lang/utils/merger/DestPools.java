package wint.lang.utils.merger;

import wint.lang.magic.Transformer;

import java.util.Collection;
import java.util.Map;

/**
 * User: huangsongli
 * Date: 15/3/4
 * Time: 上午10:20
 */
public class DestPools {

    public static <K, D> DestPool<K, D> toDestPool(Map<K, D> map) {
        return new MapDestPool<K, D>(map);
    }

    public static <K, D> DestPool<K, D> toDestPool(Collection<D> collection, Transformer<D, K> objectToKey) {
        return new CollectionDestPool<K, D>(collection, objectToKey);
    }

}
