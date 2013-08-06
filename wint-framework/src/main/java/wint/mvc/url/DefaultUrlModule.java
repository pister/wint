package wint.mvc.url;


public class DefaultUrlModule implements UrlModule {

	private UrlBrokerService urlBrokerService;
	
	private String defaultPath;
	
	private String tokenName;
	
	public DefaultUrlModule(UrlBrokerService urlBrokerService, String path, String tokenName) {
		super();
		this.urlBrokerService = urlBrokerService;
		this.defaultPath = UrlBrokerUtil.trimLastSlash(path);
		this.tokenName = tokenName;
	}
	
	public UrlBroker setTarget(String target) {
		UrlBroker urlBroker = new UrlBroker(urlBrokerService, defaultPath, target, tokenName);
		return urlBroker;
	}
	
	public String render() {
		return defaultPath;
	}

}
