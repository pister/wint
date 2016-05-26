package wint.mvc.servlet;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * User: huangsongli
 * Date: 16/5/23
 * Time: 下午1:58
 */
public class ServletUtilTest extends TestCase {

    public void test1() {
        System.out.println(ServletUtil.parseHostnameFromUrl("http://127.0.0.1:8080/avvv/aa.htm?cccc=ddd"));
        System.out.println(ServletUtil.parseHostnameFromUrl("http://127.0.0.1:8080/avvv/aa.htm"));
        System.out.println(ServletUtil.parseHostnameFromUrl("http://127.0.0.1:8080/"));
        System.out.println(ServletUtil.parseHostnameFromUrl("http://127.0.0.1:8080"));
        System.out.println(ServletUtil.parseHostnameFromUrl("http://127.0.0.1:8080?ccc=ddd"));
        System.out.println(ServletUtil.parseHostnameFromUrl("http://127.0.0.1:8080?ccc=ddd"));
        System.out.println(ServletUtil.parseHostnameFromUrl("http://127.0.0.1?ccc=ddd"));
        System.out.println(ServletUtil.parseHostnameFromUrl("http://127.0.0.1/?ccc=ddd"));
        System.out.println(ServletUtil.parseHostnameFromUrl("http://127.0.0.1/vvvv?ccc=ddd"));
        System.out.println(ServletUtil.parseHostnameFromUrl("http://127.0.0.1/vvvv?ccc=ddd"));
        System.out.println(ServletUtil.parseHostnameFromUrl("127.0.0.1"));
        System.out.println(ServletUtil.parseHostnameFromUrl("https://127.0.0.1/123"));
    }

    public void test2() {
        Assert.assertEquals("aaa", ServletUtil.getHostnameL2("aaa.bb.com"));
        Assert.assertEquals("aaa.cc", ServletUtil.getHostnameL2("aaa.cc.bb.com"));
        Assert.assertEquals("aaa",ServletUtil.getHostnameL2("aaa.bb.com.cn"));
        Assert.assertEquals("aaa.cc",ServletUtil.getHostnameL2("aaa.cc.bb.com.cn"));
        Assert.assertEquals(null,ServletUtil.getHostnameL2("bb.net"));
        Assert.assertEquals(null,ServletUtil.getHostnameL2("bb.net.cn"));
        Assert.assertEquals("cc",ServletUtil.getHostnameL2("cc.bb.net.cn"));
        Assert.assertEquals("aa.cc",ServletUtil.getHostnameL2("aa.cc.bb.net.cn"));
    }

}
