package wint.mvc.pipeline.valves;

import wint.core.config.Constants;
import wint.lang.magic.MagicMap;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.holder.WintContext;
import wint.mvc.parameters.MapParameters;
import wint.mvc.parameters.Parameters;
import wint.mvc.pipeline.PipelineContext;
import wint.mvc.servlet.ServletUtil;
import wint.mvc.url.config.UrlContext;
import wint.mvc.url.rewrite.RequestData;
import wint.mvc.url.rewrite.UrlRewriteParser;
import wint.mvc.url.rewrite.UrlRewriteService;
import wint.mvc.url.rewrite.domain.DomainParser;

import java.util.List;
import java.util.Map;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午4:53
 */
public class UrlRewriteParseValve extends AbstractValve {

    private UrlRewriteService urlRewriteService;
    private UrlContext urlContext;

    @Override
    public void init() {
        super.init();
        urlRewriteService = serviceContext.getService(UrlRewriteService.class);
        MagicMap properties = serviceContext.getConfiguration().getProperties();
        String urlSuffix = properties.getString(Constants.PropertyKeys.URL_SUFFIX, Constants.Defaults.URL_SUFFIX);
        String argumentSeparater = properties.getString(Constants.PropertyKeys.URL_ARGUMENT_SEPARATER, Constants.Defaults.URL_ARGUMENT_SEPARATER);
        urlContext = new UrlContext();
        urlContext.setUrlSuffix(urlSuffix);
        urlContext.setArgumentSeparater(argumentSeparater);
    }

    @Override
    public void invoke(PipelineContext pipelineContext, InnerFlowData innerFlowData) {
        DomainParser domainParser = urlRewriteService.getDomainParser();
        if (domainParser != null) {
            applyDomainParser(domainParser, innerFlowData);
        }
        List<UrlRewriteParser> parsers = urlRewriteService.getParsers();
        if (CollectionUtil.isEmpty(parsers)) {
            pipelineContext.invokeNext(innerFlowData);
            return;
        }
        try {
            for (UrlRewriteParser parser : parsers) {
                if (parser.matches(innerFlowData) && apply(parser, innerFlowData)) {
                    break;
                }
            }
        } finally {
            pipelineContext.invokeNext(innerFlowData);
        }
    }

    protected boolean applyDomainParser(DomainParser domainParser, InnerFlowData innerFlowData) {
        Object value = domainParser.parse(ServletUtil.getRequestHostname(WintContext.getRequest()));
        if (value == null) {
            return false;
        }
        Map<String, String[]> valueMap = MapUtil.newHashMap();
        valueMap.put(domainParser.getParameterName(), new String[]{value.toString()});
        MapParameters newParameters = new MapParameters(valueMap);
        MapParameters oldParameters = new MapParameters(innerFlowData.getParameters());
        newParameters.addParameters(oldParameters);
        innerFlowData.setParameters(newParameters);
        return true;
    }

    protected boolean apply(UrlRewriteParser parser, InnerFlowData innerFlowData) {
        RequestData requestData = parser.parse(innerFlowData, urlContext);
        if (requestData == null) {
            return false;
        }
        String target = StringUtil.trimToEmpty(requestData.getTarget());
        innerFlowData.setTarget(target);
        Parameters parsedParameters = requestData.getParameters();
        if (parsedParameters != null) {
            // 显式的queryString优先
            MapParameters newParameters = new MapParameters(parsedParameters);
            newParameters.addParameters(innerFlowData.getParameters());
            innerFlowData.setParameters(newParameters);
        }
        return true;
    }

}
