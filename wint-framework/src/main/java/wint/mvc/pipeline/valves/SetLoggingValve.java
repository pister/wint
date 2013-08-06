package wint.mvc.pipeline.valves;

import wint.core.service.env.Environment;
import wint.core.service.initial.EnvironmentAwire;
import wint.lang.WintException;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.pipeline.PipelineContext;

/**
 * @author pister 2012-3-5 07:00:29
 */
public class SetLoggingValve extends AbstractValve implements EnvironmentAwire {

	private Environment environment;
	
	public void invoke(PipelineContext pipelineContext, InnerFlowData flowData) {
		try {
			pipelineContext.invokeNext(flowData);
		} catch (Throwable e) {
			log.error("invoke valve error", e);
			if (environment == Environment.DEV) {
				throw new WintException(e);
			}
		}
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
