package wint.mvc.url.rewrite.resovler;

import wint.lang.magic.Transformer;
import wint.lang.utils.*;
import wint.mvc.url.UrlBrokerUtil;
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

    private static Transformer<Object, String> transformer = UrlBrokerUtil.getTransformer();

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

    @Override
    public void setDefaultDomainL2Value(String defaultDomainL2Value) {
    }
}
