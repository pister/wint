package wint.mvc.pipeline.valves;

import wint.mvc.flow.InnerFlowData;
import wint.mvc.pipeline.PipelineContext;

public interface Valve {

	String getName();
	
	void invoke(PipelineContext pipelineContext, InnerFlowData innerFlowData);

	String getLabel();

	void setLabel(String label);
}
