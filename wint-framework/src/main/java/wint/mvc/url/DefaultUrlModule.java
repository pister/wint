package wint.mvc.url;


import wint.lang.convert.ConvertUtil;
import wint.lang.utils.NumberUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.Tuple;

public class DefaultUrlModule implements UrlModule {

    private String moduleName;
    private UrlBrokerService urlBrokerService;
    private String path;
    private String tokenName;
    private String pathAsTargetName;

    public DefaultUrlModule(String moduleName, UrlBrokerService urlBrokerService, String path, String tokenName, String pathAsTargetName) {
        super();
        this.moduleName = moduleName;
        this.urlBrokerService = urlBrokerService;
        this.path = UrlBrokerUtil.trimLastSlash(path);
        this.tokenName = tokenName;
        this.pathAsTargetName = pathAsTargetName;
    }

    public UrlBroker appendPath(Object inputPath) {
        boolean pathHasNumber = NumberUtil.isNumeric(ConvertUtil.toString(inputPath));
        UrlBroker urlBroker = new UrlBroker(moduleName, urlBrokerService, UrlBrokerUtil.appendPath(this.path, inputPath), pathAsTargetName, tokenName, pathAsTargetName, pathHasNumber);
        return urlBroker;
    }

    public UrlBroker setTarget(String target) {
        Tuple<String, String> pathAndTarget = UrlBrokerUtil.parseTarget(path, target, pathAsTargetName);
        return new UrlBroker(moduleName, urlBrokerService, pathAndTarget.getT1(), pathAndTarget.getT2(), tokenName, pathAsTargetName, false);
    }

    public String render() {
        return path;
    }

}
