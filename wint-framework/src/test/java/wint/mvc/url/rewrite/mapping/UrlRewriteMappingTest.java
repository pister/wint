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


    public void testRender() throws Exception {
        UrlRewriteMapping urlRewriteMapping = UrlRewriteMapping.parseFromString("/hello/abc/:id/!name/age");
        UrlBroker urlBroker = new UrlBroker(null, null, "https://mydomain.com", "hello/abc", null, null, false);
        urlBroker.param("id", 2);
        urlBroker.param("name", "hsl");
        urlBroker.param("age", 21);
        UrlContext urlContext = new UrlContext();
        urlContext.setArgumentSeparator("-");
        urlContext.setUrlSuffix(".htm");
        System.out.println(urlRewriteMapping.getBaseTarget());
        System.out.println(urlRewriteMapping.matches(urlBroker));
        String url = urlRewriteMapping.rewrite(urlBroker, urlContext.getUrlSuffix());
        System.out.println(url);
    }


    public void testRenderSlash() {
        UrlRewriteMapping urlRewriteMapping = UrlRewriteMapping.parseFromString("food:id/name");
        UrlBroker urlBroker = new UrlBroker(null, null, "https://mydomain.com", "food", null, null, false);
        urlBroker.paramSeo("name", "hsl jack");
        urlBroker.param("id", 123);
        UrlContext urlContext = new UrlContext();
        urlContext.setArgumentSeparator("/");
        urlContext.setUrlSuffix(".htm");
        System.out.println(urlRewriteMapping.matches(urlBroker));
        String url = urlRewriteMapping.rewrite(urlBroker, urlContext.getUrlSuffix());
        System.out.println(url);
    }

    public void testRenderSlash2() {
        UrlRewriteMapping urlRewriteMapping = UrlRewriteMapping.parseFromString("categoryFood:id/name");
        UrlBroker urlBroker = new UrlBroker(null, null, "https://mydomain.com", "categoryFood", null, null, false);
        urlBroker.paramSeo("name", "hsl jack");
        urlBroker.param("id", 123);
        UrlContext urlContext = new UrlContext();
        urlContext.setArgumentSeparator("/");
        urlContext.setUrlSuffix(".htm");
        System.out.println(urlRewriteMapping.matches(urlBroker));
        String url = urlRewriteMapping.rewrite(urlBroker,  urlContext.getUrlSuffix());
        System.out.println(url);
    }

    public void testParse() {
        UrlRewriteMapping urlRewriteMapping = UrlRewriteMapping.parseFromString("helloJack/abc-name:id/!name/age");
        UrlContext urlContext = new UrlContext();
        urlContext.setArgumentSeparator("-");
        urlContext.setUrlSuffix(".htm");
        System.out.println(urlRewriteMapping.getBaseTarget());
        System.out.println(urlRewriteMapping.matches("/helloJack/abc-name"));
        System.out.println(urlRewriteMapping.matches("/hello-jack/abcName/"));
        System.out.println(urlRewriteMapping.matches("hello-jack/abc-name/"));
        System.out.println(urlRewriteMapping.matches("helloJack/abcName"));
        RequestData requestData = urlRewriteMapping.parse("hello-jack/abcName/21/aHNs/52.htm", urlContext);
        System.out.println(requestData);
    }

    public void testParse2() {
        UrlRewriteMapping urlRewriteMapping = UrlRewriteMapping.parseFromString("category-food:id/name");
        UrlContext urlContext = new UrlContext();
        urlContext.setArgumentSeparator("-");
        urlContext.setUrlSuffix(".htm");
        System.out.println(urlRewriteMapping.getBaseTarget());
        RequestData requestData = urlRewriteMapping.parse("category-food/6/second-category.htm", urlContext);
        System.out.println(requestData);
    }

}
