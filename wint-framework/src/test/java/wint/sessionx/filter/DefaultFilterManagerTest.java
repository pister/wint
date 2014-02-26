package wint.sessionx.filter;

import junit.framework.TestCase;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:26
 */
public class DefaultFilterManagerTest extends TestCase {

    public void test() {
        DefaultFilterManager defaultFilterManager = new DefaultFilterManager();
        defaultFilterManager.addFilter(new Filter() {
            public void doFilter(FilterContext filterContext) {
                System.out.println("first");
                filterContext.invokeNext();
                System.out.println("first end");
            }
        });
        defaultFilterManager.addFilter(new Filter() {
            public void doFilter(FilterContext filterContext) {
                System.out.println("second");
                filterContext.invokeNext();
                System.out.println("second end");
            }
        });

        defaultFilterManager.performFilters(null, null);
    }


}
