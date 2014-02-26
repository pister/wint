package wint.sessionx.filter.filters;

import wint.sessionx.filter.Filter;
import wint.sessionx.filter.FilterContext;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午2:16
 */
public class CommitFilter implements Filter {
    public void doFilter(FilterContext filterContext) {

        filterContext.invokeNext();
    }
}
