package wint.mvc.template;

import java.util.Map;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.lang.magic.MagicList;
import wint.lang.utils.MapUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.runtime.InputFormFactory;
import wint.mvc.parameters.TemplateArguments;
import wint.mvc.parameters.TemplateParameters;
import wint.mvc.template.widget.WidgetContainer;
import wint.mvc.template.widget.WidgetContainerService;
import wint.mvc.tools.service.PullToolsService;
import wint.mvc.url.UrlBrokerService;
import wint.mvc.url.UrlModule;

public class DefaultInternerVariableService extends AbstractService implements InternerVariableService {

	private WidgetContainerService widgetContainerService;
	
	private UrlBrokerService urlBrokerService;
	
	private PullToolsService pullService;
	
	private String containerName;
	
	private String paramsName;
	
	private String argsName;
	
	@Override
	public void init() {
		super.init();
		widgetContainerService = serviceContext.getService(WidgetContainerService.class);
		urlBrokerService = serviceContext.getService(UrlBrokerService.class);
		pullService = serviceContext.getService(PullToolsService.class);
		containerName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WIDGET_CONTAINER_NAME, Constants.Defaults.WIDGET_CONTAINER_NAME);
		paramsName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.PARAMS_NAME, Constants.Defaults.PARAMS_NAME);
		argsName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.ARGS_NAME, Constants.Defaults.ARGS_NAME);
	}

	public Map<String, Object> createInternerVariables(InnerFlowData flowData, Context innerContext, MagicList<Object> indexedParamters) {
		Map<String, Object> ret = MapUtil.newHashMap();
		
		if (widgetContainerService != null) {
			WidgetContainer widgetContainer = widgetContainerService.createContainer(flowData, innerContext, indexedParamters);
			ret.put(containerName, widgetContainer);
		}
		
		TemplateParameters params = new TemplateParameters(flowData.getParameters());
		TemplateArguments arguments = new TemplateArguments(flowData.getArguments());
		
		ret.put(paramsName, params);
		ret.put(argsName, arguments);
		ret.put(Constants.Form.TEMPLATE_FORM_FACTORY_NAME, new InputFormFactory(flowData));
		
		Map<String, UrlModule> urlModules = urlBrokerService.getUrlModules();
		ret.putAll(urlModules);
		
		Map<String, Object> pullTools = pullService.getPullTools();
		
		ret.putAll(pullTools);
		
		return ret;
	}

}
