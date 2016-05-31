package wint.help.autoreload.form;

import wint.core.service.env.Environment;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.DefaultFormService;
import wint.mvc.form.Form;

/**
 * 支持自动加载form(仅开发环境)，其他环境无效
 * @author pister
 * 2012-5-16 下午11:01:23
 * @deprecated
 */
public class AutoReloadFormServiceSupport extends DefaultFormService {

	private AutoReloadFormService autoReloadFormService;
	
	private Environment environment;
	
	@Override
	public Form getForm(String name, InnerFlowData flowData) {
		if (!environment.isSupportDev()) {
			return super.getForm(name, flowData);
		} else {
			return autoReloadFormService.getForm(name, flowData);
		}
	}

	@Override
	public void init() {
		super.init();
		environment = serviceContext.getConfiguration().getEnvironment();
		if (!environment.isSupportDev()) {
			return;
		}
		autoReloadFormService = new AutoReloadFormService(serviceContext);
	}

}
