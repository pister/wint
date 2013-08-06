package wint.mvc.url.config;

import wint.mvc.url.UrlBrokerUtil;

public class BaseUrlConfig extends AbstractUrlConfig {

	private String serverUrl;
	
	public BaseUrlConfig(String name, String serverUrl) {
		super(name);
		this.serverUrl = UrlBrokerUtil.normalizePath(serverUrl);
	}

	@Override
	public String getPath() {
		return serverUrl;
	}

	@Override
	public void buildPath() {
		
	}

}
