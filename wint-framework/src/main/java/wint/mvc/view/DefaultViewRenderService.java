package wint.mvc.view;

import java.util.List;
import java.util.Map;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.core.service.env.Environment;
import wint.lang.magic.MagicClass;
import wint.lang.utils.LibUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.engine.TemplateEngine;
import wint.mvc.view.types.ViewTypes;

public class DefaultViewRenderService extends AbstractService implements ViewRenderService {

	private Map<String, TemplateEngine> namedViewRenderEngines = MapUtil.newHashMap();
	
	private Map<String, ViewRender> viewRenders = MapUtil.newHashMap();
	
	private ViewRender defaultViewRender;
	
	private DefaultViewRenderEngine defaultViewRenderEngine = new DefaultViewRenderEngine();

    private boolean debugViewSupport;

    private String debugParamName;

    private Environment environment;

	@Override
	public void init() {
		super.init();
		defaultViewRenderEngine.init(serviceContext);
		defaultViewRender = viewRenders.get(ViewTypes.TEMPLATE_VIEW_TYPE);

        debugViewSupport = serviceContext.getConfiguration().getProperties().getBoolean(Constants.PropertyKeys.WINT_DEBUG_VIEW_SUPPORT, Constants.Defaults.WINT_DEBUG_VIEW_SUPPORT);
        debugParamName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WINT_DEBUG_VIEW_PARAM_NAME, Constants.Defaults.WINT_DEBUG_VIEW_PARAM_NAME);

        environment = serviceContext.getConfiguration().getEnvironment();
		initViewRenderEngines();
	}
	
	private void initViewRenderEngines() {
		if (LibUtil.isFreeMarkerExist()) {
			MagicClass templateEngineClass = MagicClass.forName("wint.mvc.template.engine.freemark.FreeMarkerTemplateEngine");
			TemplateEngine templateEngine = (TemplateEngine)templateEngineClass.newInstance().getObject();
			templateEngine.init(serviceContext);
			namedViewRenderEngines.put(templateEngine.getName(), templateEngine);
		}
		if (LibUtil.isVelocityExist()) {
			MagicClass templateEngineClass = MagicClass.forName("wint.mvc.template.engine.velocity.VelocityTemplateEngine");
			TemplateEngine templateEngine = (TemplateEngine)templateEngineClass.newInstance().getObject();
			templateEngine.init(serviceContext);
			namedViewRenderEngines.put(templateEngine.getName(), templateEngine);
		}
        if (LibUtil.isHttlExist()) {
            MagicClass templateEngineClass = MagicClass.forName("wint.mvc.template.engine.httl.HttlTemplateEngine");
            TemplateEngine templateEngine = (TemplateEngine)templateEngineClass.newInstance().getObject();
            templateEngine.init(serviceContext);
            namedViewRenderEngines.put(templateEngine.getName(), templateEngine);
        }
        // TODO jsp template
	}
	
	public TemplateEngine getTemplateEngine(String name) {
		TemplateEngine viewRenderEngine = namedViewRenderEngines.get(name);
		if (viewRenderEngine != null) {
			return viewRenderEngine;
		}
		return defaultViewRenderEngine;
	}

	public ViewRender getViewRender(InnerFlowData flowData, String viewType) {
        if (environment != Environment.PRODUCT && debugViewSupport) {
            String debugView = flowData.getParameters().getString(debugParamName);
            if (!StringUtil.isEmpty(debugView)) {
                ViewRender debugViewRender = viewRenders.get(debugView);
                if (debugViewRender != null) {
                    return debugViewRender;
                }
            }
        }
		return viewRenders.get(viewType);
	}
	
	public ViewRender getDefaultViewRender() {
		return defaultViewRender;
	}

	public void setViewRenders(List<ViewRender> viewRenders) {
		for (ViewRender viewRender : viewRenders ) {
			this.viewRenders.put(viewRender.getViewType(), viewRender);
		}
	}


}
