package wint.mvc.url.rewrite;

import wint.mvc.url.UrlBroker;
import wint.mvc.url.config.UrlContext;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午1:19
 */
public interface UrlRewriteHandle {

    String rewrite(UrlBroker urlBroker, UrlContext urlContext);

    boolean matches(UrlBroker urlBroker);


}
