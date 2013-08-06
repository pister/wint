package wint.mvc.pipeline.samplevalves;

import wint.core.service.initial.Initializor;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.pipeline.PipelineContext;
import wint.mvc.pipeline.valves.AbstractValve;

public class HelloValve extends AbstractValve implements Initializor {

	public void invoke(PipelineContext pipelineContext, InnerFlowData flowData) {
		System.out.println("say hello from " + this);
		pipelineContext.invokeNext(flowData);
	}

	public void init() {
		System.out.println("hello initialize at " + this);
	}

}
