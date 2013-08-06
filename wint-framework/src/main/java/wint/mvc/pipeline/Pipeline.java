package wint.mvc.pipeline;

import java.util.List;

import wint.lang.utils.CollectionUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.pipeline.valves.Valve;

/**
 * @author pister
 * 2011-12-28 06:04:47
 */
public class Pipeline {
	
	private List<Valve> valves = CollectionUtil.newArrayList();
	
	protected String name;

	public void execute(InnerFlowData flowData) {
		PipelineContext pipelineContext = new PipelineContext(valves);
		executeValves(pipelineContext, flowData);
	}
	
	private void executeValves(PipelineContext pipelineContext, InnerFlowData flowData) {
		pipelineContext.invokeNext(flowData);
	}
	
	public void setValves(List<Valve> valves) {
		this.valves = valves;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
