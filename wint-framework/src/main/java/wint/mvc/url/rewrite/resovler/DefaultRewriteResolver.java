package wint.mvc.url.rewrite.resovler;

import wint.lang.magic.Transformer;
import wint.lang.utils.*;
import wint.mvc.url.rewrite.mapping.Item;
import wint.mvc.url.rewrite.resovler.RewriteResolver;
import wint.mvc.url.rewrite.UrlBase64;

import java.util.Collection;
import java.util.Date;

/**
 * User: huangsongli
 * Date: 16/5/23
 * Time: 下午4:35
 */
public class DefaultRewriteResolver implements RewriteResolver {

    private static final String CHARSET = "utf-8";

    private static Transformer<Object, String> transformer;

    static {
        transformer = new Transformer<Object, String>() {
            public String transform(Object object) {
                if (object == null) {
                    return StringUtil.EMPTY;
                }
                if (object instanceof String) {
                    String stringValue = (String)object;
                    stringValue = UrlUtil.encode(stringValue, CHARSET);
                    return stringValue;
                } if (object instanceof Date) {
                    String stringValue = DateUtil.formatDate(object, "yyyyMMddHHmmss");
                    return stringValue;
                } else if (object.getClass().isArray()) {
                    return ArrayUtil.join(object, ",");
                } else if (object instanceof Collection) {
                    Collection<?> c = (Collection<?>)object;
                    return CollectionUtil.join(c, ",");
                } else {
                    return object.toString();
                }
            }
        };
    }

    @Override
    public String toQueryData(Item item, Object value) {
        if (value == null) {
            return null;
        }
        String stringValue = transformer.transform(value);
        if (item.isBase64()) {
            return UrlBase64.encodeBase64(stringValue);
        }
        return stringValue;
    }

    @Override
    public String fromQueryData(Item item, String input) {
        if (input == null) {
            return null;
        }
        if (item.isBase64()) {
            return UrlBase64.decodeBase64(input);
        }
        return input;
    }
}
