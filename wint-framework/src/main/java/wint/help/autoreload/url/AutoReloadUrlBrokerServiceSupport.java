package wint.help.autoreload.url;

import java.util.Map;

import wint.core.service.env.Environment;
import wint.mvc.url.DefaultUrlBrokerService;
import wint.mvc.url.UrlModule;

/**
 * 自动加载url配置（仅开发模式）
 * @author pister
 * 2012-5-16 下午11:18:01
 */
public class AutoReloadUrlBrokerServiceSupport extends DefaultUrlBrokerService {

	private AutoReloadUrlBrokerService autoReloadUrlBrokerService;
	
	private Environment environment;
	
	@Override
	public Map<String, UrlModule> getUrlModules() {
		if (!environment.isSupportDev()) {
			return super.getUrlModules();
		} else {
			return autoReloadUrlBrokerService.getUrlModules();
		}
	}

	
	@Override
	public void init() {
		super.init();
		environment = serviceContext.getConfiguration().getEnvironment();
		if (!environment.isSupportDev()) {
			return;
		}
		autoReloadUrlBrokerService = new AutoReloadUrlBrokerService(this, serviceContext);
	}
}
