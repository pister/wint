package wint.mvc.pipeline.valves;

import wint.core.config.Constants;
import wint.lang.magic.MagicList;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.module.ExecutionModule;
import wint.mvc.module.Module;
import wint.mvc.pipeline.PipelineContext;
import wint.mvc.template.Context;
import wint.mvc.view.ViewRender;
import wint.mvc.view.ViewRenderService;

/**
 * @author pister 2012-1-2 12:10:05
 */
public class PerformActionValve extends AbstractValve {

	private ViewRenderService viewRenderService;
	
	private static final String LOAD_MODULE_VALVE_LABEL = "loadModule";
	
	public void init() {
		super.init();
		viewRenderService = serviceContext.getService(ViewRenderService.class);
	}

	public void invoke(PipelineContext pipelineContext, InnerFlowData flowData) {
		perform(pipelineContext, flowData);
		pipelineContext.invokeNext(flowData);
	}
	
	@SuppressWarnings("unchecked")
	private void perform(PipelineContext pipelineContext, InnerFlowData flowData) {
		ExecutionModule module = (ExecutionModule)flowData.getModule();
		Context context = flowData.getContext();
		
		MagicList<Object> indexedParamters = (MagicList<Object>)flowData.getAttribute(Constants.FlowDataAttributeKeys.INDEXED_PARAMETERS);
		String target = module.execute(flowData, context, indexedParamters);
		
		if (flowData.isSendRedirected()) {
			if (log.isDebugEnabled()) {
				log.debug("the flow data is send redirect, break the pipeline.");
			}
			pipelineContext.breakPipeline();
		} else if (flowData.isForwardTo()) {
			flowData.resetRedirected();
			pipelineContext.invokeTo(LOAD_MODULE_VALVE_LABEL, flowData);
		} else {
			// 如果是doAction
			// 根据执行的结果找到对应的screen action
			// 然后执行screen action
			if (module.isDoAction()) {
				performDoAction(pipelineContext, flowData, module, target);
			} else {
				String viewType = flowData.getViewType();
				if (StringUtil.isEmpty(viewType)) {
					ViewRender viewRender = viewRenderService.getDefaultViewRender();
					viewRender.render(context, flowData, target, module.getType());
				} else {
					ViewRender viewRender = viewRenderService.getViewRender(flowData, viewType);
					if (viewRender == null) {
						viewRender = viewRenderService.getDefaultViewRender();
					} 
					viewRender.render(context, flowData, target, module.getType());
				}
			}
		}
	}
	
	private void performDoAction(PipelineContext pipelineContext, InnerFlowData flowData, Module module, String returnResult) {
		// 如果是action，找到相应的page
		String screenTarget = getActualTarget(module, flowData, returnResult);
		if (log.isDebugEnabled()) {
			log.debug("the module is action, redirect to " + screenTarget);
		}
		flowData.setTarget(screenTarget);
		// 取消可能的重定向标记，那么在DoAction中使用的时候flowData.setTarget和flowData.setRedirectTarget将是一样的效果
		flowData.resetRedirected();
		// 执行相应的page
		pipelineContext.invokeTo(LOAD_MODULE_VALVE_LABEL, flowData);
	}
	
	/**
	 * 按如下顺序找：1、rundata.Target的值, 2、返回值，注解默认值，3、和Module同名
	 * @param module
	 * @param flowData
	 * @param returnResult
	 * @return
	 */
	private String getActualTarget(Module module, FlowData flowData, String returnResult) {
		String result = flowData.getTarget();
		if (!StringUtil.isEmpty(result)) {
			return result;
		}
		if (!StringUtil.isEmpty(returnResult)) {
			return returnResult;
		}
		return module.getDefaultTarget();
	}
	

}
