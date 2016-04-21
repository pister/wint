package wint.lang.utils.filter;

/**
 * User: huangsongli
 * Date: 16/4/21
 * Time: 上午10:27
 */
public interface Filter<T> {

    boolean accept(T t);

}
