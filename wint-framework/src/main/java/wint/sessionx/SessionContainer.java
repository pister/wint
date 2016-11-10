package wint.sessionx;

import wint.lang.magic.MagicMap;
import wint.lang.misc.profiler.Profiler;
import wint.lang.utils.MapUtil;
import wint.sessionx.constants.AttrKeys;
import wint.sessionx.filter.DefaultFilterManager;
import wint.sessionx.filter.Filter;
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

    public void init(SessionProvider sessionProvider, MagicMap initParamters, ServletContext servletContext) {
        requestFilterManager = new DefaultFilterManager(initParamters);
        responseFilterManager = new DefaultFilterManager(initParamters);


        addRequestFilter(new InitializeFilter(servletContext));
        addRequestFilter(new ParseRequestFilter(sessionProvider.getRequestParser()));
        addRequestFilter(new CreateSessionStoreFilter(sessionProvider.getSessionStoreCreator()));
        addRequestFilter(new EnsureStatusFilter());
        addRequestFilter(new CreateNewRequestResponseFilter());

        addResponseFilter(new CommitFilter());
    }

    private void addRequestFilter(Filter filter) {
        requestFilterManager.addFilter(new ProfilerProxyFilter(filter));
    }

    private void addResponseFilter(Filter filter) {
        responseFilterManager.addFilter(new ProfilerProxyFilter(filter));
    }

    private static class ProfilerProxyFilter implements Filter {

        private Filter target;

        public ProfilerProxyFilter(Filter target) {
            this.target = target;
        }

        @Override
        public String getName() {
            return target.getName();
        }

        @Override
        public void init(MagicMap initParamters) {
            target.init(initParamters);
        }

        @Override
        public void doFilter(FilterContext filterContext) {
            try {
                Profiler.enter(target.getName() + " doFilter");
                target.doFilter(filterContext);
            } finally {
                Profiler.release();
            }
        }
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response, WintSessionProcessor.ProcessorHandler processorHandler) throws IOException, ServletException {
        try {
            Profiler.enter("SessionContainer handleRequest");
            Map<String, Object> attributes = MapUtil.newHashMap();
            attributes.put(AttrKeys.RAW_REQUEST, request);
            attributes.put(AttrKeys.RAW_RESPONSE, response);
            FilterContext filterContext = requestFilterManager.performFilters(attributes);
            HttpServletRequest newRequest = (HttpServletRequest) filterContext.getAttribute(AttrKeys.NEW_REQUEST);
            HttpServletResponse newResponse = (HttpServletResponse) filterContext.getAttribute(AttrKeys.NEW_RESPONSE);
            processorHandler.onProcess(newRequest, newResponse);
            responseFilterManager.performFilters(filterContext.getAttributes());
        } finally {
            Profiler.release();
        }
    }

}
