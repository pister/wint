package wint.help.seo;

import junit.framework.TestCase;
import org.junit.Assert;

/**
 * @author songlihuang
 * @date 2024/10/12 10:56
 */
public class SeoStringUtilTest extends TestCase {

    public void test1() {
        String s = SeoStringUtil.toFriendlyInUrl("Why do evangelicals interpret Heb 4:12 with a meaning that ascribes animacy and agency to a bunch of words?");
        System.out.println(s);
    }

    public void testSymbols() {
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello!world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello@world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello#world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello$world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello%world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello^world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello&world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello*world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello(world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello)world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello-world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello_world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello+world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello=world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello{world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello}world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello[world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello]world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello:world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello;world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello\"world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello'world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello<world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello>world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello,world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello.world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello?world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello/world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello|world"));
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello\\world"));
    }

    public void testMultiSymbols() {
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("hello-| world"));
    }

    public void testMultiWorld() {
        Assert.assertEquals("hello-world-jack", SeoStringUtil.toFriendlyInUrl("hello-| world jack"));
    }

    public void testUpperCaseSymbols() {
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("Hello World"));
    }

    public void testEndSymbol() {
        Assert.assertEquals("hello-world", SeoStringUtil.toFriendlyInUrl("Hello World.?"));
    }

    public void testEndNotAscii() {
        Assert.assertEquals("hello-world-%E4%BD%A0%E5%A5%BD", SeoStringUtil.toFriendlyInUrl("Hello World.你好"));
    }
}