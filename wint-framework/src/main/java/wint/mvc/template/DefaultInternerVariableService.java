package wint.mvc.template;

import java.util.Arrays;
import java.util.Map;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.core.service.aop.ProfilerInterceptor;
import wint.core.service.env.Environment;
import wint.lang.magic.MagicList;
import wint.lang.magic.MagicObject;
import wint.lang.utils.MapUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.runtime.InputFormFactory;
import wint.mvc.i18n.ResourceBundleService;
import wint.mvc.i18n.ResourceBundleServiceWrapper;
import wint.mvc.parameters.Parameters;
import wint.mvc.parameters.TemplateArguments;
import wint.mvc.parameters.TemplateParameters;
import wint.mvc.template.widget.WidgetContainer;
import wint.mvc.template.widget.WidgetContainerService;
import wint.mvc.template.widget.outer.OuterWidgetContainer;
import wint.mvc.template.widget.outer.OuterWidgetContainerService;
import wint.mvc.tools.service.PullToolsService;
import wint.mvc.url.UrlBrokerService;
import wint.mvc.url.UrlModule;

public class DefaultInternerVariableService extends AbstractService implements InternerVariableService {

	private WidgetContainerService widgetContainerService;

    private OuterWidgetContainerService outerWidgetContainerService;
	
	private UrlBrokerService urlBrokerService;
	
	private PullToolsService pullService;

    private ResourceBundleService resourceBundleService;
	
	private String containerName;

	private String outerContainerName;

	private String paramsName;
	
	private String argsName;

    private String targetName;

    private String i18n;

    private Environment environment;

    private String environmentName;

    private Map<String, Object> pullTools;
	
	@Override
	public void init() {
		super.init();
		widgetContainerService = serviceContext.getService(WidgetContainerService.class);
		urlBrokerService = serviceContext.getService(UrlBrokerService.class);
		pullService = serviceContext.getService(PullToolsService.class);
		resourceBundleService = serviceContext.getService(ResourceBundleService.class);
        outerWidgetContainerService = serviceContext.getService(OuterWidgetContainerService.class);

        environment = serviceContext.getConfiguration().getEnvironment();

        containerName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WIDGET_CONTAINER_NAME, Constants.Defaults.WIDGET_CONTAINER_NAME);
        outerContainerName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WIDGET_OUTER_CONTAINER_NAME, Constants.Defaults.WIDGET_OUTER_CONTAINER_NAME);
		paramsName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.PARAMS_NAME, Constants.Defaults.PARAMS_NAME);
		argsName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.ARGS_NAME, Constants.Defaults.ARGS_NAME);
		targetName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.TARGET_NAME, Constants.Defaults.TARGET_NAME);
        i18n = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WINT_I18N_VAR_NAME, Constants.Defaults.WINT_I18N_VAR_NAME);
        environmentName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WINT_ENVIRONMENT_VAR_NAME, Constants.Defaults.WINT_ENVIRONMENT_VAR_NAME);
        // TODO environment

        Map<String, Object> rawPullTools = pullService.getPullTools();
        Map<String, Object> proxyPullTolls = MapUtil.newHashMap();

        ProfilerInterceptor profilerInterceptor = new ProfilerInterceptor();
        for (Map.Entry<String, Object> entry : rawPullTools.entrySet()) {
            String name = entry.getKey();
            Object o = entry.getValue();
            MagicObject mo = MagicObject.wrap(o);
            proxyPullTolls.put(name, mo.asProxyObject(Arrays.asList(profilerInterceptor)).getObject());
        }

        pullTools = proxyPullTolls;


    }

	public Map<String, Object> createInternerVariables(InnerFlowData flowData, Context innerContext, MagicList<Object> indexedParameters) {
		Map<String, Object> ret = MapUtil.newHashMap();
		
		if (widgetContainerService != null) {
			WidgetContainer widgetContainer = widgetContainerService.createContainer(flowData, innerContext, indexedParameters);
			ret.put(containerName, widgetContainer);
		}

        if (outerWidgetContainerService != null) {
            OuterWidgetContainer outerWidgetContainer = outerWidgetContainerService.createContainer(flowData, innerContext);
            ret.put(outerContainerName, outerWidgetContainer);
        }

        Parameters params = flowData.getParameters();
		TemplateArguments arguments = new TemplateArguments(flowData.getArguments());
		
		ret.put(paramsName, params);
		ret.put(argsName, arguments);
		ret.put(environmentName, environment);
		ret.put(Constants.Form.TEMPLATE_FORM_FACTORY_NAME, new InputFormFactory(flowData));
		
		Map<String, UrlModule> urlModules = urlBrokerService.getUrlModules();
		ret.putAll(urlModules);

        ResourceBundleServiceWrapper resourceBundleServiceWrapper = new ResourceBundleServiceWrapper(flowData, resourceBundleService);

        ret.put(i18n, resourceBundleServiceWrapper);
        ret.put(targetName, flowData.getTarget());

		ret.putAll(pullTools);
		
		return ret;
	}

}
