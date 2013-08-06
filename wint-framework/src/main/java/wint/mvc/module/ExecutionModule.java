package wint.mvc.module;

import wint.lang.magic.MagicList;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.Context;

public interface ExecutionModule extends Module {

	String execute(InnerFlowData flowData, Context context, MagicList<Object> indexedParameters);
	
}
