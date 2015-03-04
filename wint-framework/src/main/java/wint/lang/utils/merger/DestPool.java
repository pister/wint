package wint.lang.utils.merger;

import java.util.Map;

/**
 * User: huangsongli
 * Date: 15/3/4
 * Time: 上午10:01
 */
public interface DestPool<K, D> {

    Map<K, D> toMap();

}
