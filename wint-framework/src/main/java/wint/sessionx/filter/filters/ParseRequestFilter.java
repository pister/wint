package wint.sessionx.filter.filters;

import wint.sessionx.constants.AttrKeys;
import wint.sessionx.filter.Filter;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.provider.RequestParser;

import javax.servlet.http.HttpServletRequest;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:21
 */
public class ParseRequestFilter extends AbstractFilter {

    private RequestParser requestParser;

    public ParseRequestFilter(RequestParser requestParser) {
        this.requestParser = requestParser;
    }

    public void doFilter(FilterContext filterContext) {
        HttpServletRequest request = (HttpServletRequest)filterContext.getAttribute(AttrKeys.RAW_REQUEST);
        Object parseResult = requestParser.parseRequest(request.getCookies());
        filterContext.setAttribute(AttrKeys.PARSE_REQUEST_RESULT, parseResult);
        filterContext.invokeNext();
    }
}
