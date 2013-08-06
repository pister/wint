package wint.mvc.pipeline;

import java.util.Map;

import wint.core.service.AbstractService;
import wint.lang.utils.MapUtil;

public class DefaultPipelineService extends AbstractService implements PipelineService {

	private Map<String, Pipeline> pipelines = MapUtil.newHashMap();
	
	public DefaultPipelineService() {
		super();
	}

	public Pipeline getPipeline(String pipelineName) {
		return pipelines.get(pipelineName);
	}

	public void setPipelines(Map<String, Pipeline> pipelines) {
		this.pipelines = pipelines;
	}
	
	
}
