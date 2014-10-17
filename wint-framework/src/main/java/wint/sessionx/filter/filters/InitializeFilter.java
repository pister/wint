package wint.sessionx.filter.filters;

import wint.sessionx.constants.AttrKeys;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.provider.sessionid.SessionIdGenerators;

import javax.servlet.ServletContext;

/**
 * Created by pister on 14-2-26.
 */
public class InitializeFilter extends AbstractFilter {

    private ServletContext servletContext;

    public InitializeFilter(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void doFilter(FilterContext filterContext) {
        filterContext.setAttribute(AttrKeys.SERVLET_CONTEXT, servletContext);
        filterContext.setAttribute(AttrKeys.SESSION_ID_GENERATOR, SessionIdGenerators.getSessionIdGenerator());
        filterContext.invokeNext();
    }
}
