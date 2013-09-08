package wint.mvc.url;


import wint.lang.utils.StringUtil;

public class DefaultUrlModule implements UrlModule {

	private UrlBrokerService urlBrokerService;
	
	private String path;
	
	private String tokenName;

    private String pathAsTargetName;
	
	public DefaultUrlModule(UrlBrokerService urlBrokerService, String path, String tokenName, String pathAsTargetName) {
		super();
		this.urlBrokerService = urlBrokerService;
		this.path = UrlBrokerUtil.trimLastSlash(path);
		this.tokenName = tokenName;
        this.pathAsTargetName = pathAsTargetName;
	}
	
	public UrlBroker setTarget(String target) {
        if (pathAsTargetName.equals(target)) {
            int lastSlashPos = path.lastIndexOf("/");
            if (lastSlashPos < 0) {
                // 没有/的情况
                UrlBroker urlBroker = new UrlBroker(urlBrokerService, path, StringUtil.EMPTY, tokenName);
                return urlBroker;
            }
            int firstSlash = target.indexOf("/");
            if (lastSlashPos == firstSlash + 1) {
                // http://hostname
                // https://hostname
                UrlBroker urlBroker = new UrlBroker(urlBrokerService, path, StringUtil.EMPTY, tokenName);
                return urlBroker;
            }
            String targetPath = path.substring(0, lastSlashPos);
            String newTarget = path.substring(lastSlashPos + 1) ;
            // http://hostname/abc/efg
            // => http://hostname/abc and efg
            UrlBroker urlBroker = new UrlBroker(urlBrokerService, targetPath, newTarget, tokenName);
            return urlBroker;

        } else {
		    UrlBroker urlBroker = new UrlBroker(urlBrokerService, path, target, tokenName);
		    return urlBroker;
        }
	}
	
	public String render() {
		return path;
	}

}
