package wint.mvc.pipeline.valves;

import wint.core.config.Constants;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.module.LoadModuleService;
import wint.mvc.module.Module;
import wint.mvc.pipeline.PipelineContext;

/**
 * @author pister
 * 2012-12-10 下午10:06:33
 */
public class LoadModuleValve extends AbstractValve {

	private LoadModuleService loadModuleService;
	
	private String moduleActionName;
	
	public void init() {
		super.init();
		loadModuleService = serviceContext.getService(LoadModuleService.class);
		moduleActionName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.APP_PACKAGE_WEB_ACTION, Constants.Defaults.APP_PACKAGE_WEB_ACTION);
	}

	public void invoke(PipelineContext pipelineContext, InnerFlowData innerFlowData) {
		Module module = loadModuleService.loadModule(innerFlowData.getTarget(), moduleActionName, innerFlowData);
		innerFlowData.setModule(module);
		pipelineContext.invokeNext(innerFlowData);
	}

}
