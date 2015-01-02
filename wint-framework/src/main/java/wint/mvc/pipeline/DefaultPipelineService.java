package wint.mvc.pipeline;

import java.util.Map;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;

public class DefaultPipelineService extends AbstractService implements PipelineService {

	private Map<String, Pipeline> pipelines = MapUtil.newHashMap();

    private String urlSuffix;

	public DefaultPipelineService() {
		super();
	}

    @Override
    public void init() {
        super.init();
        urlSuffix = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.URL_SUFFIX, Constants.Defaults.URL_SUFFIX);
        if (urlSuffix != null) {
            urlSuffix = urlSuffix.toLowerCase();
        }
    }

    public Pipeline getPipeline(String pipelineName) {
        if (StringUtil.equals("." + pipelineName, urlSuffix)) {
            return pipelines.get(Constants.Defaults.PIPELINE_NAME);
        }
        Pipeline pipeline = pipelines.get(pipelineName);
        if (pipeline != null) {
            return pipeline;
        }
        return pipelines.get(Constants.Defaults.PIPELINE_NAME);
    }

	public void setPipelines(Map<String, Pipeline> pipelines) {
		this.pipelines = pipelines;
	}
	
	
}
