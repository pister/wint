package wint.mvc.template.widget;

import java.util.Map;

import wint.lang.magic.MagicList;
import wint.lang.utils.MapUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.flow.WidgetInnerFlowData;
import wint.mvc.module.ExecutionModule;
import wint.mvc.template.Context;
import wint.mvc.template.DefaultContext;
import wint.mvc.template.LoadTemplateService;
import wint.mvc.template.TemplateRender;
import wint.mvc.view.Render;

public class WidgetRender implements Render {

	private ExecutionModule widgetModule;
	
	private InnerFlowData flowData;
	
	private Context context;
	
	private MagicList<Object> indexedParameters;
	
	private String templateName;
	
	private String widgetTemplateName;
	
	private LoadTemplateService loadTemplateService;
	
	private Map<String, Object> contextValues = MapUtil.newHashMap();
	
	public WidgetRender(ExecutionModule module, InnerFlowData flowData, Context context, MagicList<Object> indexedParameters, String templateName, String widgetTemplateName, LoadTemplateService loadTemplateService) {
		super();
		this.widgetModule = module;
		this.flowData = flowData;
		this.context = context;
		this.indexedParameters = indexedParameters;
		this.templateName = templateName;
		this.loadTemplateService = loadTemplateService;
		this.widgetTemplateName = widgetTemplateName;
	}

	public String render() {
		Context widgetContext = new DefaultContext(context);
		widgetContext.putAll(contextValues);
		
		widgetModule.execute(new WidgetInnerFlowData(flowData, widgetModule), widgetContext, indexedParameters);
		
		TemplateRender templateRender = loadTemplateService.loadTemplate(templateName, widgetContext, widgetTemplateName);
		if (templateRender == null) {
			return null;
		}
		return templateRender.render();
	}
	
	public WidgetRender addToContext(String name, Object o) {
		contextValues.put(name, o);
		return this;
	}

}
