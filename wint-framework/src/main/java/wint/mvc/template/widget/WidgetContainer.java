package wint.mvc.template.widget;

import wint.lang.magic.MagicList;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.module.ExecutionModule;
import wint.mvc.module.LoadModuleService;
import wint.mvc.template.Context;
import wint.mvc.template.LoadTemplateService;
import wint.mvc.view.Render;


public class WidgetContainer implements Render {
	
	private LoadTemplateService loadTemplateService;
	
	private LoadModuleService loadModuleService;
	
	private String widgetName;
	
	private String widgetTemplateName;
	
	private InnerFlowData flowData;
	
	private Context context;
	
	private MagicList<Object> indexedParameters;
	
	public WidgetContainer(LoadTemplateService loadTemplateService, LoadModuleService loadModuleService, String widgetName, String widgetTemplateName, InnerFlowData flowData, Context context, MagicList<Object> indexedParameters) {
		super();
		this.loadTemplateService = loadTemplateService;
		this.loadModuleService = loadModuleService;
		this.widgetName = widgetName;
		this.widgetTemplateName = widgetTemplateName;
		this.flowData = flowData;
		this.context = context;
		this.indexedParameters = indexedParameters;
	}

	public WidgetRender setTemplate(String templateName) {
		templateName = StringUtil.getFirstBefore(templateName, ".");
		ExecutionModule widgetModule = loadModuleService.loadModule(templateName, widgetName);
		WidgetRender ret = new WidgetRender(widgetModule, flowData, context, indexedParameters, templateName, widgetTemplateName, loadTemplateService);
		return ret;
	}

	public String render() {
		return "$Widget$";
	}

}
