package wint.mvc.url.config;

import java.util.Map;

import junit.framework.TestCase;
import wint.lang.util.ResourceUtil;

public class XmlUrlConfigParserTest extends TestCase {
	
	public void testParse() {
		XmlUrlConfigParser xmlUrlConfigParser = new XmlUrlConfigParser();
		Map<String, AbstractUrlConfig> m = xmlUrlConfigParser.parse(ResourceUtil.getInputStream("url.xml"));
		System.out.println(m);
	}

}
