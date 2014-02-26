package wint.sessionx;

import wint.lang.utils.MapUtil;
import wint.sessionx.filter.AttrKeys;
import wint.sessionx.filter.DefaultFilterManager;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.filter.FilterManager;
import wint.sessionx.filter.filters.CommitFilter;
import wint.sessionx.filter.filters.CreateNewRequestResponseFilter;
import wint.sessionx.filter.filters.CreateSessionStoreFilter;
import wint.sessionx.filter.filters.ParseRequestFilter;
import wint.sessionx.provider.SessionProvider;

import javax.servlet.FilterChain;
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

    public void init(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;

        requestFilterManager = new DefaultFilterManager();
        requestFilterManager.addFilter(new ParseRequestFilter(sessionProvider.getRequestParser()));
        requestFilterManager.addFilter(new CreateSessionStoreFilter(sessionProvider.getSessionStoreCreator()));
        requestFilterManager.addFilter(new CreateNewRequestResponseFilter());

        responseFilterManager = new DefaultFilterManager();
        responseFilterManager.addFilter(new CommitFilter());
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Map<String, Object> attributes = MapUtil.newHashMap();
        attributes.put(AttrKeys.RAW_REQUEST, request);
        attributes.put(AttrKeys.RAW_RESPONSE, response);
        FilterContext filterContext = requestFilterManager.performFilters(attributes);
        HttpServletRequest newRequest = (HttpServletRequest)filterContext.getAttribute(AttrKeys.NEW_REQUEST);
        HttpServletResponse newResponse = (HttpServletResponse)filterContext.getAttribute(AttrKeys.NEW_RESPONSE);
        chain.doFilter(newRequest, newResponse);
        responseFilterManager.performFilters(filterContext.getAttributes());
    }

}
