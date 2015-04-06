package wint.mvc.url.rewrite.mapping;

import junit.framework.TestCase;
import wint.mvc.url.UrlBroker;
import wint.mvc.url.config.UrlContext;
import wint.mvc.url.rewrite.RequestData;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午8:03
 */
public class UrlRewriteMappingTest extends TestCase {

    UrlRewriteMapping urlRewriteMapping = UrlRewriteMapping.parseFromString("hello/abc/name-age-page-siteAddress", "-");

    public void testRender() throws Exception {
        UrlBroker urlBroker = new UrlBroker(null, "http://127.0.0.1", "hello/abc", null, null, false);
      //  urlBroker.param("name", "hsl");
      //  urlBroker.param("age", 21);
      //  urlBroker.param("page", 2);
        urlBroker.param("siteAddress", "hangzhou");
        UrlContext urlContext = new UrlContext();
        urlContext.setArgumentSeparater("-");
        urlContext.setUrlSuffix(".htm");
        System.out.println(urlRewriteMapping.matches(urlBroker));
        String url = urlRewriteMapping.rewrite(urlBroker, urlContext);
        System.out.println(url);
    }

    public void testParse() {
        UrlContext urlContext = new UrlContext();
        urlContext.setArgumentSeparater("-");
        urlContext.setUrlSuffix(".htm");
        System.out.println(urlRewriteMapping.matches("/hello/abc"));
        System.out.println(urlRewriteMapping.matches("/hello/abc/"));
        System.out.println(urlRewriteMapping.matches("hello/abc/"));
        System.out.println(urlRewriteMapping.matches("hello/abc"));
        RequestData requestData = urlRewriteMapping.parse("hello/abc/hsl-21--xxx.htm", urlContext);
        System.out.println(requestData);
    }

}
