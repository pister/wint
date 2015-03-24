package wint.mvc.url.rewrite;

import wint.mvc.flow.FlowData;
import wint.mvc.url.config.UrlContext;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午4:41
 */
public interface UrlRewriteParser {

    RequestData parse(FlowData flowData, UrlContext urlContext);

    boolean matches(FlowData flowData);

}

