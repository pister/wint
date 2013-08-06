package wint.mvc.template.widget;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.lang.magic.MagicList;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.module.LoadModuleService;
import wint.mvc.template.Context;
import wint.mvc.template.LoadTemplateService;

public class DefaultWidgetContainerService extends AbstractService implements WidgetContainerService {

	private LoadTemplateService loadTemplateService;
	
	private LoadModuleService loadModuleService;
	
	private String widgetName;
	
	private String widgetTemplateName;
	
	@Override
	public void init() {
		super.init();
		loadTemplateService = (LoadTemplateService)this.serviceContext.getService(LoadTemplateService.class);
		loadModuleService = (LoadModuleService)this.serviceContext.getService(LoadModuleService.class);
		widgetName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.APP_PACKAGE_WEB_WIDGET, Constants.Defaults.APP_PACKAGE_WEB_WIDGET);
		widgetTemplateName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.TEMPLATE_WIDGET, Constants.Defaults.TEMPLATE_WIDGET);
	}

	public WidgetContainer createContainer(InnerFlowData flowData, Context context, MagicList<Object> indexedParameters) {
		WidgetContainer ret = new WidgetContainer(loadTemplateService, loadModuleService, widgetName, widgetTemplateName, flowData, context, indexedParameters);
		return ret;
	}

}
