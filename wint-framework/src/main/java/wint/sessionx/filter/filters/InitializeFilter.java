package wint.sessionx.filter.filters;

import wint.sessionx.filter.AttrKeys;
import wint.sessionx.filter.Filter;
import wint.sessionx.filter.FilterContext;

import javax.servlet.ServletContext;

/**
 * Created by pister on 14-2-26.
 */
public class InitializeFilter implements Filter {

    private ServletContext servletContext;

    public InitializeFilter(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void doFilter(FilterContext filterContext) {
        filterContext.setAttribute(AttrKeys.SERVLET_CONTEXT, servletContext);
    }
}
