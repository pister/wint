package wint.mvc.module;

import wint.core.service.Service;
import wint.mvc.flow.InnerFlowData;

/**
 * @author pister 2012-1-2 03:17:00
 */
public interface LoadModuleService extends Service {

	ExecutionModule loadModule(String target, String moduleType, InnerFlowData innerFlowData);
	
	
}
