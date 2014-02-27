package wint.sessionx;

import wint.lang.magic.MagicMap;
import wint.lang.utils.MapUtil;
import wint.sessionx.constants.AttrKeys;
import wint.sessionx.filter.DefaultFilterManager;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.filter.FilterManager;
import wint.sessionx.filter.filters.*;
import wint.sessionx.provider.SessionProvider;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:31
 */
public class SessionContainer {

    private FilterManager requestFilterManager;

    private FilterManager responseFilterManager;

    private SessionProvider sessionProvider;

    public void init(SessionProvider sessionProvider, MagicMap initParamters, ServletContext servletContext) {
        this.sessionProvider = sessionProvider;

        requestFilterManager = new DefaultFilterManager(initParamters);
        requestFilterManager.addFilter(new InitializeFilter(servletContext));
        requestFilterManager.addFilter(new ParseRequestFilter(sessionProvider.getRequestParser()));
        requestFilterManager.addFilter(new CreateSessionStoreFilter(sessionProvider.getSessionStoreCreator()));
        requestFilterManager.addFilter(new EnsureStatusFilter());
        requestFilterManager.addFilter(new CreateNewRequestResponseFilter());

        responseFilterManager = new DefaultFilterManager(initParamters);
        responseFilterManager.addFilter(new CommitFilter());
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response, WintSessionProcessor.ProcessorHandler processorHandler) throws IOException, ServletException {
        Map<String, Object> attributes = MapUtil.newHashMap();
        attributes.put(AttrKeys.RAW_REQUEST, request);
        attributes.put(AttrKeys.RAW_RESPONSE, response);
        FilterContext filterContext = requestFilterManager.performFilters(attributes);
        HttpServletRequest newRequest = (HttpServletRequest)filterContext.getAttribute(AttrKeys.NEW_REQUEST);
        HttpServletResponse newResponse = (HttpServletResponse)filterContext.getAttribute(AttrKeys.NEW_RESPONSE);
        processorHandler.onProcess(newRequest, newResponse);
        responseFilterManager.performFilters(filterContext.getAttributes());
    }

}
