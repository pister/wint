package wint.mvc.url;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.lang.magic.MagicMap;
import wint.lang.magic.Transformer;
import wint.lang.utils.*;
import wint.mvc.url.config.AbstractUrlConfig;
import wint.mvc.url.config.DefaultUrlConfigLoader;
import wint.mvc.url.config.UrlConfigLoader;

/**
 * @author pister 2012-3-2 11:57:48
 */
public class DefaultUrlBrokerService extends AbstractService implements UrlBrokerService {

	private String urlSuffix;
	
	private String argumentSeparater;
	
	private Transformer<Object, String> transformer;
	
	private UrlConfigLoader urlConfigLoader;
	
	private Map<String, UrlModule> urlModules;
	
	private String tokenName;

    private String pathAsTargetName;
	
	@Override
	public void init() {
		super.init();
		MagicMap properties = serviceContext.getConfiguration().getProperties();
		urlSuffix = properties.getString(Constants.PropertyKeys.URL_SUFFIX, Constants.Defaults.URL_SUFFIX);
		argumentSeparater = properties.getString(Constants.PropertyKeys.URL_ARGUMENT_SEPARATER, Constants.Defaults.URL_ARGUMENT_SEPARATER);
		tokenName = properties.getString(Constants.PropertyKeys.CSRF_TOKEN_NAME, Constants.Defaults.CSRF_TOKEN_NAME);
        pathAsTargetName = properties.getString(Constants.PropertyKeys.URL_PATH_AS_TARGET_NAME, Constants.Defaults.URL_PATH_AS_TARGET_NAME);
		final String charset = properties.getString(Constants.PropertyKeys.CHARSET_ENCODING, Constants.Defaults.CHARSET_ENCODING);
		transformer = new Transformer<Object, String>() {
			public String transform(Object object) {
				if (object == null) {
					return StringUtil.EMPTY;
				}
				if (object instanceof String) {
					String stringValue = (String)object;
					stringValue = UrlUtil.encode(stringValue, charset);
					return stringValue;
				} if (object instanceof Date) {
                    String stringValue = DateUtil.formatFullDate(object);
                    stringValue = UrlUtil.encode(stringValue, charset);
                    return stringValue;
                } else if (object.getClass().isArray()) {
                    Object[] array = (Object[])object;
                    return ArrayUtil.join(array, ",");
                } else if (object instanceof Collection) {
                    Collection<?> c = (Collection<?>)object;
                    return CollectionUtil.join(c, ",");
                } else {
					return object.toString();
				}
			}
		};
		
		urlConfigLoader = new DefaultUrlConfigLoader(serviceContext);
		
		Map<String, AbstractUrlConfig> urlConfigs = urlConfigLoader.loadUrlModules();
		urlModules = MapUtil.newHashMap();
		for (Map.Entry<String, AbstractUrlConfig> entry : urlConfigs.entrySet()) {
			urlModules.put(entry.getKey(), new DefaultUrlModule(this, entry.getValue().getPath(), tokenName, pathAsTargetName));
		}
		
	}
	
	public UrlBroker makeUrlBroker(String urlModuleName, String target) {
		UrlModule urlModule = urlModules.get(urlModuleName);
		if (urlModule== null) {
			return null;
		}
		return urlModule.setTarget(target);
	}

	public Map<String, UrlModule> getUrlModules() {
		return urlModules;
	}

	public String render(UrlBroker urlBroker) {
		return UrlBrokerUtil.renderUrlBroker(urlBroker, urlSuffix, argumentSeparater, transformer);
	}

}
