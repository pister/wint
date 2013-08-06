package wint.mvc.pipeline;

import wint.core.service.Service;

public interface PipelineService extends Service {
	
	Pipeline getPipeline(String pipelineName);

}
