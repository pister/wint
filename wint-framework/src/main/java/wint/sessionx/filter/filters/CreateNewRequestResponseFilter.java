package wint.sessionx.filter.filters;

import wint.sessionx.constants.AttrKeys;
import wint.sessionx.filter.Filter;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.servlet.WintSessionHttpServletRequest;
import wint.sessionx.servlet.WintSessionHttpServletResponse;
import wint.sessionx.store.SessionStore;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午2:15
 */
public class CreateNewRequestResponseFilter extends AbstractFilter {

    public void doFilter(FilterContext filterContext) {
        HttpServletRequest request = (HttpServletRequest)filterContext.getAttribute(AttrKeys.RAW_REQUEST);
        HttpServletResponse response = (HttpServletResponse)filterContext.getAttribute(AttrKeys.RAW_RESPONSE);
        SessionStore sessionStore = (SessionStore)filterContext.getAttribute(AttrKeys.SESSION_STORE);
        ServletContext servletContext = (ServletContext)filterContext.getAttribute(AttrKeys.SERVLET_CONTEXT);

        WintSessionHttpServletRequest wintSessionHttpServletRequest = new WintSessionHttpServletRequest(request, servletContext, sessionStore);
        WintSessionHttpServletResponse wintSessionHttpServletResponse = new WintSessionHttpServletResponse(response);
        filterContext.setAttribute(AttrKeys.NEW_REQUEST, wintSessionHttpServletRequest);
        filterContext.setAttribute(AttrKeys.NEW_RESPONSE, wintSessionHttpServletResponse);

        filterContext.invokeNext();
    }
}
