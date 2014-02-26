package wint.sessionx.filter.filters;

import wint.sessionx.filter.AttrKeys;
import wint.sessionx.filter.Filter;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.provider.RequestParser;

import javax.servlet.http.HttpServletRequest;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:21
 */
public class ParseRequestFilter implements Filter {

    private RequestParser requestParser;

    public ParseRequestFilter(RequestParser requestParser) {
        this.requestParser = requestParser;
    }

    public void doFilter(FilterContext filterContext) {
        HttpServletRequest request = (HttpServletRequest)filterContext.getAttribute(AttrKeys.RAW_REQUEST);
        Object parseRequest = requestParser.parseRequest(request.getCookies());
        filterContext.setAttribute(AttrKeys.PARSE_REQUEST_RESULT, parseRequest);
        filterContext.invokeNext();
    }
}
