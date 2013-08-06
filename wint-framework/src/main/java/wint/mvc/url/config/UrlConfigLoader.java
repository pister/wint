package wint.mvc.url.config;

import java.util.Map;

public interface UrlConfigLoader {
	
	Map<String, AbstractUrlConfig> loadUrlModules();

}
