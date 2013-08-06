package wint.mvc.template;

import wint.core.service.Service;
import wint.lang.magic.MagicList;
import wint.mvc.flow.InnerFlowData;

/**
 * @author pister 2012-1-2 12:09:28
 */
public interface ContextService extends Service {
	
	Context createContext(InnerFlowData flowData, MagicList<Object> indexedParamters);

}
