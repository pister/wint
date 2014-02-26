package wint.sessionx.filter.filters;

import wint.sessionx.filter.AttrKeys;
import wint.sessionx.filter.Filter;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.servlet.WintSessionHttpServletRequest;
import wint.sessionx.servlet.WintSessionHttpServletResponse;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午2:16
 */
public class CommitFilter implements Filter {

    public void doFilter(FilterContext filterContext) {
        try {
            filterContext.invokeNext();
        } finally {
            WintSessionHttpServletRequest wintSessionHttpServletRequest = (WintSessionHttpServletRequest) filterContext.getAttribute(AttrKeys.NEW_REQUEST);
            WintSessionHttpServletResponse wintSessionHttpServletResponse = (WintSessionHttpServletResponse) filterContext.getAttribute(AttrKeys.NEW_RESPONSE);
            // 注意顺序，先提交request,然后提交response

            wintSessionHttpServletRequest.commit(filterContext);
            wintSessionHttpServletResponse.commit(filterContext);
        }
    }



}
