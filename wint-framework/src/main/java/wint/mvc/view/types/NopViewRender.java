package wint.mvc.view.types;

import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.Context;
import wint.mvc.view.ViewRender;

public class NopViewRender implements ViewRender {

	public String getViewType() {
		return ViewTypes.NOP_VIEW_TYPE;
	}

	public void render(Context context, InnerFlowData flowData, String target, String moduleType) {
		// do nothing
	}

}
