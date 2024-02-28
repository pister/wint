package wint.sessionx;

import wint.core.config.Constants;
import wint.core.config.property.MagicPropertiesMap;
import wint.core.config.property.PropertiesMap;
import wint.lang.magic.MagicMap;
import wint.mvc.servlet.ServletUtil;
import wint.sessionx.provider.SessionProvider;
import wint.sessionx.provider.SessionProviderFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:52
 */
public class WintSessionFilter implements Filter {

    private SessionContainer sessionContainer;

    public void init(FilterConfig filterConfig) throws ServletException {
        sessionContainer = new SessionContainer();
        PropertiesMap properties = new MagicPropertiesMap(ServletUtil.getInitParameters(filterConfig));
        String sessionType = properties.getString(Constants.PropertyKeys.WINT_SESSION_TYPE, Constants.Defaults.WINT_SESSION_TYPE);
        filterConfig.getServletContext().log("Wint sessionType: " + sessionType);
        SessionProvider sessionProvider = SessionProviderFactory.getSessionProvider(sessionType);
        sessionProvider.init(properties, filterConfig.getServletContext());
        sessionContainer.init(sessionProvider, properties, filterConfig.getServletContext());
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        sessionContainer.handleRequest(httpRequest, httpResponse, new FilterProcessorHandler(chain));
    }

    public void destroy() {
    }

    public static class FilterProcessorHandler implements WintSessionProcessor.ProcessorHandler {

        private FilterChain chain;

        public FilterProcessorHandler(FilterChain chain) {
            super();
            this.chain = chain;
        }

        public void onProcess(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
            chain.doFilter(httpRequest, httpResponse);
        }

    }
}
