package wint.mvc.url.config;

import java.io.InputStream;
import java.util.Map;

public interface UrlConfigParser {
	
	 Map<String, AbstractUrlConfig> parse(InputStream is);
}
