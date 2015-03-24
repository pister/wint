package wint.mvc.pipeline.valves;

import wint.core.config.Constants;
import wint.lang.magic.MagicMap;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.parameters.Arguments;
import wint.mvc.parameters.MapParameters;
import wint.mvc.parameters.Parameters;
import wint.mvc.pipeline.PipelineContext;
import wint.mvc.url.config.UrlContext;
import wint.mvc.url.rewrite.RequestData;
import wint.mvc.url.rewrite.UrlRewriteParser;
import wint.mvc.url.rewrite.UrlRewriteService;

import java.util.List;

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

    protected boolean apply(UrlRewriteParser parser, InnerFlowData innerFlowData) {
        RequestData requestData = parser.parse(innerFlowData, urlContext);
        if (requestData == null) {
            return false;
        }
        String target = requestData.getTarget();
        if (!StringUtil.isEmpty(target)) {
            innerFlowData.setTarget(target);
        }
        Parameters parsedParameters = requestData.getParameters();
        if (parsedParameters != null) {
            MapParameters newParameters = new MapParameters(innerFlowData.getParameters());
            newParameters.addParameters(parsedParameters);
            innerFlowData.setParameters(newParameters);
        }
        return true;
    }

}
