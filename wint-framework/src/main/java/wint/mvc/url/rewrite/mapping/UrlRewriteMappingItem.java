package wint.mvc.url.rewrite.mapping;

import wint.mvc.flow.FlowData;
import wint.mvc.url.UrlBroker;
import wint.mvc.url.config.UrlContext;
import wint.mvc.url.rewrite.RequestData;
import wint.mvc.url.rewrite.UrlRewriteHandle;
import wint.mvc.url.rewrite.UrlRewriteParser;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午8:53
 */
public class UrlRewriteMappingItem implements UrlRewriteParser, UrlRewriteHandle {

    private UrlRewriteMapping urlRewriteMapping;

    public UrlRewriteMappingItem(UrlRewriteMapping urlRewriteMapping) {
        this.urlRewriteMapping = urlRewriteMapping;
    }

    @Override
    public String rewrite(UrlBroker urlBroker, UrlContext urlContext) {
        return urlRewriteMapping.rewrite(urlBroker, urlContext);
    }

    @Override
    public boolean matches(UrlBroker urlBroker) {
        return urlRewriteMapping.matches(urlBroker);
    }

    @Override
    public RequestData parse(FlowData flowData, UrlContext urlContext) {
        return urlRewriteMapping.parse(flowData.getTarget(), urlContext);
    }

    @Override
    public boolean matches(FlowData flowData) {
        return urlRewriteMapping.matches(flowData.getTarget());
    }
}
