package wint.mvc.pipeline.valves;

import wint.mvc.flow.InnerFlowData;
import wint.mvc.pipeline.PipelineContext;

public class NopValve extends AbstractValve {

	public void invoke(PipelineContext pipelineContext, InnerFlowData flowData) {
		pipelineContext.invokeNext(flowData);
	}

}
