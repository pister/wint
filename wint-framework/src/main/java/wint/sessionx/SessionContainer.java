package wint.sessionx;

import wint.core.config.Constants;
import wint.core.config.property.MagicPropertiesMap;
import wint.core.config.property.PropertiesMap;
import wint.lang.magic.MagicMap;
import wint.lang.misc.profiler.Profiler;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.TargetUtil;
import wint.mvc.servlet.ServletUtil;
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
import java.util.Set;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:31
 */
public class SessionContainer {

    private FilterManager requestFilterManager;

    private FilterManager responseFilterManager;

    private Set<String> ignorePaths;

    private boolean isPathIgnore(HttpServletRequest httpRequest) {
        if (CollectionUtil.isEmpty(ignorePaths)) {
            return false;
        }
        String path = ServletUtil.getServletPath(httpRequest);
        String target = TargetUtil.normalizeTarget(path);
        return ignorePaths.contains(target);
    }

    public void init(SessionProvider sessionProvider, PropertiesMap initParameters, ServletContext servletContext) {
        requestFilterManager = new DefaultFilterManager(initParameters);
        responseFilterManager = new DefaultFilterManager(initParameters);

        addRequestFilter(new InitializeFilter(servletContext));
        addRequestFilter(new ParseRequestFilter(sessionProvider.getRequestParser()));
        addRequestFilter(new CreateSessionStoreFilter(sessionProvider.getSessionStoreCreator()));
        addRequestFilter(new EnsureStatusFilter());
        addRequestFilter(new CreateNewRequestResponseFilter());
        addResponseFilter(new CommitFilter());

        String ignorePaths = initParameters.getString(Constants.PropertyKeys.WINT_SESSION_IGNORE_PATHS);
        if (!StringUtil.isEmpty(ignorePaths)) {
            this.ignorePaths = CollectionUtil.newHashSet(StringUtil.splitTrim(ignorePaths, ","));
        }
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
        public void init(PropertiesMap initParameters) {
            target.init(initParameters);
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
        if (isPathIgnore(request)) {
            processorHandler.onProcess(request, response);
        } else {
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

}
