package wint.mvc.url;

import wint.core.config.Constants;
import wint.core.config.property.PropertiesMap;
import wint.core.service.AbstractService;
import wint.lang.magic.Transformer;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.mvc.url.config.AbstractUrlConfig;
import wint.mvc.url.config.DefaultUrlConfigLoader;
import wint.mvc.url.config.UrlConfigLoader;
import wint.mvc.url.config.UrlContext;
import wint.mvc.url.rewrite.UrlRewriteHandle;
import wint.mvc.url.rewrite.UrlRewriteService;
import wint.mvc.url.rewrite.domain.DomainRewriteHandle;
import wint.mvc.url.rewrite.domain.DomainRewriteUrlBroker;

import java.util.List;
import java.util.Map;

/**
 * @author pister 2012-3-2 11:57:48
 */
public class DefaultUrlBrokerService extends AbstractService implements UrlBrokerService {

    private String urlSuffix;
    private String argumentSeparator;
    private Transformer<Object, String> transformer;
    private UrlConfigLoader urlConfigLoader;
    private Map<String, UrlModule> urlModules;
    private String tokenName;
    private String pathAsTargetName;
    private UrlRewriteService urlRewriteService;
    private UrlContext urlContext;

    private List<UrlRewriteHandle> handles;

    @Override
    public void init() {
        super.init();
        urlRewriteService = serviceContext.getService(UrlRewriteService.class);
        PropertiesMap properties = serviceContext.getConfiguration().getProperties();
        urlSuffix = properties.getString(Constants.PropertyKeys.URL_SUFFIX, Constants.Defaults.URL_SUFFIX);
        argumentSeparator = properties.getString(Constants.PropertyKeys.URL_ARGUMENT_SEPARATOR, Constants.Defaults.URL_ARGUMENT_SEPARATOR);
        tokenName = properties.getString(Constants.PropertyKeys.CSRF_TOKEN_NAME, Constants.Defaults.CSRF_TOKEN_NAME);
        pathAsTargetName = properties.getString(Constants.PropertyKeys.URL_PATH_AS_TARGET_NAME, Constants.Defaults.URL_PATH_AS_TARGET_NAME);
        transformer = UrlBrokerUtil.getTransformer();
        urlConfigLoader = new DefaultUrlConfigLoader(serviceContext);

        Map<String, AbstractUrlConfig> urlConfigs = urlConfigLoader.loadUrlModules();
        urlModules = MapUtil.newHashMap();
        for (Map.Entry<String, AbstractUrlConfig> entry : urlConfigs.entrySet()) {
            String moduleName = entry.getKey();
            urlModules.put(moduleName, new DefaultUrlModule(moduleName, this, entry.getValue().getPath(), tokenName, pathAsTargetName));
        }

        urlContext = new UrlContext();
        urlContext.setUrlSuffix(urlSuffix);
        urlContext.setArgumentSeparator(argumentSeparator);

        handles = urlRewriteService.getHandles();
    }

    public UrlBroker makeUrlBroker(String urlModuleName, String target) {
        UrlModule urlModule = urlModules.get(urlModuleName);
        if (urlModule == null) {
            return null;
        }
        return urlModule.setTarget(target);
    }

    public Map<String, UrlModule> getUrlModules() {
        return urlModules;
    }

    protected String renderForDefault(UrlBroker urlBroker) {
        return UrlBrokerUtil.renderUrlBroker(urlBroker, urlSuffix, argumentSeparator, transformer);
    }

    protected UrlBroker makeUrlBrokerForRender(UrlBroker urlBroker) {
        if (!urlRewriteService.acceptModule(urlBroker.getModuleName())) {
            return urlBroker;
        }
        DomainRewriteHandle domainRewriteHandle = urlRewriteService.getDomainRewriteHandle();
        if (domainRewriteHandle == null) {
            return urlBroker;
        }
        return new DomainRewriteUrlBroker(urlBroker, domainRewriteHandle);
    }

    public String render(UrlBroker urlBroker) {
        urlBroker = makeUrlBrokerForRender(urlBroker);
        if (CollectionUtil.isEmpty(handles)) {
            return renderForDefault(urlBroker);
        }
        for (UrlRewriteHandle urlRewriteHandle : handles) {
            if (urlRewriteHandle.matches(urlBroker)) {
                return urlRewriteHandle.rewrite(urlBroker, urlContext);
            }
        }
        return renderForDefault(urlBroker);
    }

}
