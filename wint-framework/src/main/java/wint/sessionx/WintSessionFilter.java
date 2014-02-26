package wint.sessionx;

import wint.sessionx.filter.FilterContext;
import wint.sessionx.provider.SessionProvider;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
        SessionProvider sessionProvider = null; //TODO set default
        sessionContainer.init(sessionProvider);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        sessionContainer.handleRequest(httpRequest, httpResponse, chain);
    }

    public void destroy() {
    }
}
