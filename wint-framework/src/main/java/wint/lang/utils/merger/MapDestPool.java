package wint.lang.utils.merger;

import java.util.Map;

/**
 * User: huangsongli
 * Date: 15/3/4
 * Time: 上午10:04
 */
public class MapDestPool<K, D> implements DestPool<K, D> {

    private Map<K, D> map;

    public MapDestPool(Map<K, D> map) {
        this.map = map;
    }

    @Override
    public Map<K, D> toMap() {
        return map;
    }
}
