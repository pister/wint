package wint.mvc.servlet;

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
        System.out.println(ServletUtil.getHostnameL2("aaa.bb.com"));
        System.out.println(ServletUtil.getHostnameL2("aaa.bb.com.cn"));
        System.out.println(ServletUtil.getHostnameL2("bb.net"));
        System.out.println(ServletUtil.getHostnameL2("bb.net.cn"));
        System.out.println(ServletUtil.getHostnameL2("cc.bb.net.cn"));
    }

}
