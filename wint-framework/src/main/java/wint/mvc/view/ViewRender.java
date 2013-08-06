package wint.mvc.view;

import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.Context;

public interface ViewRender {
	
	void render(Context context, InnerFlowData flowData, String target, String moduleType);
	
	String getViewType();

}
