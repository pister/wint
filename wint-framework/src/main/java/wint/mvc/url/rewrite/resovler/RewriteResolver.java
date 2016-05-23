package wint.mvc.url.rewrite.resovler;

import wint.mvc.url.rewrite.mapping.Item;

/**
 * User: huangsongli
 * Date: 16/5/23
 * Time: 下午4:32
 */
public interface RewriteResolver {

    String toQueryData(Item item, Object value);

    String fromQueryData(Item item, String input);

}
