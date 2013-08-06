package wint.mvc.url;

import junit.framework.Assert;
import junit.framework.TestCase;
import wint.lang.magic.Transformer;
import wint.lang.utils.StringUtil;
import wint.lang.utils.UrlUtil;

public class UrlBrokerUtilTest extends TestCase {
	
	public void testNormalizePath() {
		Assert.assertEquals("", UrlBrokerUtil.normalizePath(null));
		Assert.assertEquals("", UrlBrokerUtil.normalizePath(""));
		Assert.assertEquals("", UrlBrokerUtil.normalizePath("/"));
		Assert.assertEquals("abc/", UrlBrokerUtil.normalizePath("abc"));
		Assert.assertEquals("abc/", UrlBrokerUtil.normalizePath("abc/"));
		Assert.assertEquals("abc/", UrlBrokerUtil.normalizePath("/abc/"));
		Assert.assertEquals("abc/", UrlBrokerUtil.normalizePath("\\abc\\"));
		Assert.assertEquals("abc/", UrlBrokerUtil.normalizePath("//abc/"));
		Assert.assertEquals("aaa/ccc/", UrlBrokerUtil.normalizePath("aaa/ccc"));
	}
	
	private static final Transformer<Object, String> defaultTransformer = new Transformer<Object, String>() {

		public String transform(Object object) {
			if (object == null) {
				return StringUtil.EMPTY;
			}
			if (object instanceof String) {
				String stringValue = (String)object;
				stringValue = UrlUtil.encode(stringValue, "utf-8");
				return stringValue;
			} else {
				return object.toString();
			}
		}
	};


	
}
