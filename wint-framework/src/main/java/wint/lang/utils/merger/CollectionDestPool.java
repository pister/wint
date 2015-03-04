package wint.lang.utils.merger;

import wint.lang.magic.Transformer;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;

import java.util.Collection;
import java.util.Map;

/**
 * User: huangsongli
 * Date: 15/3/4
 * Time: 上午10:05
 */
public class CollectionDestPool<K, D> implements DestPool<K, D> {

    private Map<K, D> map;

    public CollectionDestPool(Collection<D> collection, Transformer<D, K> objectToKey) {
        map = MapUtil.newHashMap();
        if (CollectionUtil.isEmpty(collection)) {
            return;
        }
        for (D d : collection) {
            K key = objectToKey.transform(d);
            map.put(key, d);
        }
    }

    @Override
    public Map<K, D> toMap() {
        return map;
    }
}
