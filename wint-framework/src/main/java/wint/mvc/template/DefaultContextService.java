package wint.mvc.template;

import wint.core.service.AbstractService;
import wint.lang.magic.MagicList;
import wint.mvc.flow.InnerFlowData;

/**
 * @author pister 2012-1-2 12:09:25
 */
public class DefaultContextService extends AbstractService implements ContextService {

	private InternerVariableService internVariableService;
	
	@Override
	public void init() {
		super.init();
		internVariableService = (InternerVariableService)serviceContext.getService(InternerVariableService.class);
	}

	public Context createContext(InnerFlowData flowData, MagicList<Object> indexedParamters) {
		Context ret = new DefaultContext();
		Context innerContext = new DefaultContext();
		Context context = new DefaultContext();
		if (internVariableService != null) { 
			innerContext.putAll(internVariableService.createInternerVariables(flowData, innerContext, indexedParamters));
			flowData.setInnerContext(innerContext);
			flowData.setContext(context);
		}
		return ret;
		
	}

}
