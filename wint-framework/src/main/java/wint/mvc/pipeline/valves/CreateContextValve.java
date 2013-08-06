package wint.mvc.pipeline.valves;

import wint.core.config.Constants;
import wint.lang.magic.MagicList;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.pipeline.PipelineContext;
import wint.mvc.template.ContextService;

/**
 * @author pister
 * 2012-12-10 下午10:06:27
 */
public class CreateContextValve extends AbstractValve {

	private ContextService contextService;
	
	public void init() {
		super.init();
		contextService = serviceContext.getService(ContextService.class);
	}
	
	@SuppressWarnings("unchecked")
	public void invoke(PipelineContext pipelineContext, InnerFlowData innerFlowData) {
		MagicList<Object> indexedParamters = (MagicList<Object>)innerFlowData.getAttribute(Constants.FlowDataAttributeKeys.INDEXED_PARAMETERS);
		contextService.createContext(innerFlowData, indexedParamters);
		
		pipelineContext.invokeNext(innerFlowData);
	}

}
