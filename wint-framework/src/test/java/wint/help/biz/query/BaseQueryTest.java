package wint.help.biz.query;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * User: huangsongli
 * Date: 16/3/25
 * Time: 下午12:55
 */
public class BaseQueryTest extends TestCase {

    public void testQuery() {
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setPageSize(100);
        baseQuery.setPageNo(1);
        Assert.assertEquals(0, baseQuery.getStartRow());
        baseQuery.setPageNo(2);
        Assert.assertEquals(100, baseQuery.getStartRow());
        baseQuery.setStartRow(350);
        Assert.assertEquals(350, baseQuery.getStartRow());
        baseQuery.setTotalResultCount(360);
        Assert.assertEquals(4, baseQuery.getPageCount());
        baseQuery.setPageNo(2);
        Assert.assertEquals(2,  baseQuery.getCurrentPage());
        Assert.assertEquals(true, baseQuery.hasNextPage());
        baseQuery.turnNext(); //3
        Assert.assertEquals(3,   baseQuery.getCurrentPage());
        baseQuery.turnNext(); // 4
        Assert.assertEquals(4,   baseQuery.getCurrentPage());
        Assert.assertEquals(false, baseQuery.hasNextPage());
        baseQuery.setPageNo(2);
        Assert.assertEquals(2, baseQuery.getCurrentPage());


        baseQuery.setStartRow(0);
        Assert.assertEquals(1, baseQuery.getCurrentPage());
        baseQuery.setStartRow(99);
        Assert.assertEquals(1, baseQuery.getCurrentPage());
        baseQuery.setStartRow(100);
        Assert.assertEquals(2, baseQuery.getCurrentPage());
        baseQuery.setStartRow(199);
        Assert.assertEquals(2, baseQuery.getCurrentPage());
        baseQuery.setStartRow(200);
        Assert.assertEquals(3, baseQuery.getCurrentPage());

        baseQuery.setPageSize(30);
        baseQuery.setPageNo(1);
        Assert.assertEquals(1, baseQuery.getPageNo());

        baseQuery.setPageSize(60);
        baseQuery.setPageNo(2);
        Assert.assertEquals(2, baseQuery.getPageNo());
    }

}
