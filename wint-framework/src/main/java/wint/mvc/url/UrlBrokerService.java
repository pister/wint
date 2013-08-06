package wint.mvc.url;

import java.util.Map;

import wint.core.service.Service;

/**
 * @author pister 2012-3-2 11:57:50
 */
public interface UrlBrokerService extends Service {
	
	String render(UrlBroker urlBroker);
	
	Map<String, UrlModule> getUrlModules();
	
	UrlBroker makeUrlBroker(String urlModuleName, String target);

}
