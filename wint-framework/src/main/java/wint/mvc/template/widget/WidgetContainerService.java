package wint.mvc.template.widget;

import wint.core.service.Service;
import wint.lang.magic.MagicList;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.Context;

public interface WidgetContainerService extends Service {
	
	WidgetContainer createContainer(InnerFlowData flowData, Context context, MagicList<Object> indexedParameters);

}
