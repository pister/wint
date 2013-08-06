package wint.mvc.pipeline.valves;

import wint.core.config.Constants;
import wint.lang.utils.TargetUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.pipeline.PipelineContext;

/**
 * @author pister 2012-2-26 12:29:14
 */
public class SetIndexValve extends AbstractValve {

	private String indexTarget = Constants.Defaults.INDEX_TARGET;
	
	public void invoke(PipelineContext pipelineContext, InnerFlowData flowData) {
		String target = flowData.getTarget();
		target = TargetUtil.normalizeTarget(target);
		if ("/".equals(target)) {
			applyDefaultIndex(flowData);
		}
		pipelineContext.invokeNext(flowData);
	}
	
	protected void applyDefaultIndex(InnerFlowData flowData) {
		flowData.setTarget(indexTarget);
	}

	public String getIndexTarget() {
		return indexTarget;
	}

	public void setIndexTarget(String indexTarget) {
		this.indexTarget = indexTarget;
	}

}
