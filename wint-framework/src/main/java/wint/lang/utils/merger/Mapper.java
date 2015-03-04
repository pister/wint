package wint.lang.utils.merger;

import java.util.List;

/**
 * User: huangsongli
 * Date: 15/3/4
 * Time: 上午10:00
 */
public interface Mapper<S, K, D> {

    /**
     * 从源业务对象中获取key
     * @param source
     * @return
     */
    K toKey(S source);

    /**
     * 通过一个keys批量转成一个目标对象pool
     * @param keys
     * @return
     */
    DestPool<K, D> toDestPool(List<K> keys);

    /**
     * 把目标对象回写到源业务对象
     * @param source
     * @param dest
     */
    void setDest(S source, D dest);

}
