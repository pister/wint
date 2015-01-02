package wint.mvc.pipeline.valves;

import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.Context;
import wint.mvc.view.ViewRender;
import wint.mvc.view.ViewRenderService;
import wint.mvc.view.types.JsonViewRender;
import wint.mvc.view.types.ViewTypes;

/**
 * User: huangsongli
 * Date: 15/1/2
 * Time: 上午8:06
 */
public class JsonCheckCsrfTokenValve extends CheckCsrfTokenValve {

    private ViewRenderService viewRenderService;

    @Override
    public void init() {
        super.init();
        viewRenderService = serviceContext.getService(ViewRenderService.class);
    }

    @Override
    protected void handleProduct(InnerFlowData innerFlowData) {
        Context context = innerFlowData.getContext();
        context.put("success", false);
        context.put("message", "miss csrf token.");
        ViewRender jsonViewRender = viewRenderService.getViewRender(innerFlowData, ViewTypes.JSON_VIEW_TYPE);
        jsonViewRender.render(context, innerFlowData, null, null);
    }

    @Override
    protected void handleDevSupport(InnerFlowData innerFlowData) {
        handleProduct(innerFlowData);
    }
}
