package wint.mvc.module;

import wint.core.service.Service;

/**
 * @author pister 2012-1-2 03:17:00
 */
public interface LoadModuleService extends Service {

	ExecutionModule loadModule(String target, String moduleType);
	
	
}
