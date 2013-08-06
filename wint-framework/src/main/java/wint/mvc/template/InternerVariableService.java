package wint.mvc.template;

import java.util.Map;

import wint.core.service.Service;
import wint.lang.magic.MagicList;
import wint.mvc.flow.InnerFlowData;

public interface InternerVariableService extends Service {
	
	Map<String, Object> createInternerVariables(InnerFlowData flowData, Context innerContext, MagicList<Object> indexedParamters);

}
