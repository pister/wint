package wint.help.autoreload.url;

import java.util.Map;
import java.util.Set;

import wint.core.config.Constants;
import wint.core.io.resource.autoload.LastModifiedFile;
import wint.core.service.ServiceContext;
import wint.help.autoreload.AbstractAutoReload;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.mvc.url.DefaultUrlModule;
import wint.mvc.url.UrlBrokerService;
import wint.mvc.url.UrlModule;
import wint.mvc.url.config.AbstractUrlConfig;
import wint.mvc.url.config.DefaultUrlConfigLoader;
import wint.mvc.url.config.UrlConfigLoader;

/**
 * 支持url配置自动加载
 * @author pister
 * 2012-5-16 下午11:24:27
 */
public class AutoReloadUrlBrokerService {
	
	private UrlBrokerService urlBrokerService;
	
	private ServiceContext serviceContext;
	
	public AutoReloadUrlBrokerService(UrlBrokerService urlBrokerService, ServiceContext serviceContext) {
		super();
		this.urlBrokerService = urlBrokerService;
		this.serviceContext = serviceContext;
		
		init();
	}

	private Map<String, UrlModule> urlModules = MapUtil.newHashMap();
	
	public AutoReloadUrlBrokerService(ServiceContext serviceContext) {
		super();
		this.serviceContext = serviceContext;
		init();
	}
	
	public void init() {
		AutoReloadUrl autoReloadUrl = new AutoReloadUrl();
		autoReloadUrl.startAutoReload();
		autoReloadUrl.onReload();
	}
	
	public Map<String, UrlModule> getUrlModules() {
		return urlModules;
	}
	
	class AutoReloadUrl extends AbstractAutoReload {

		private UrlConfigLoader urlConfigLoader = new DefaultUrlConfigLoader(serviceContext);
		
		private Set<LastModifiedFile> files = CollectionUtil.newHashSet();
		
		private String tokenName;
		
		public AutoReloadUrl() {
			String urlConfigFile = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.URL_CONFIG_FILE, Constants.Defaults.URL_CONFIG_FILE);
			tokenName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.CSRF_TOKEN_NAME, Constants.Defaults.CSRF_TOKEN_NAME);
			files.add(new LastModifiedFile(serviceContext.getResourceLoader().getResource(urlConfigFile)));
		}
		
		@Override
		protected Set<LastModifiedFile> getCheckFiles() {
			return files;
		}

		@Override
		protected synchronized void onReload() {
			Map<String, AbstractUrlConfig> urlConfigs = urlConfigLoader.loadUrlModules();
			Map<String, UrlModule> newUrlModules = MapUtil.newHashMap();
			for (Map.Entry<String, AbstractUrlConfig> entry : urlConfigs.entrySet()) {
				newUrlModules.put(entry.getKey(), new DefaultUrlModule(urlBrokerService, entry.getValue().getPath(), tokenName));
			}
			
			urlModules = newUrlModules;
			
			log.info("reload url configs success.");
		}
		
	}

}
