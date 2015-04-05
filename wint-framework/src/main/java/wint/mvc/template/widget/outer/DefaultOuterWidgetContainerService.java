package wint.mvc.template.widget.outer;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.Context;
import wint.mvc.view.DefaultViewRenderService;
import wint.mvc.view.ViewRenderService;

import java.io.File;

/**
 * User: huangsongli
 * Date: 15/1/8
 * Time: 上午10:31
 */
public class DefaultOuterWidgetContainerService extends AbstractService implements OuterWidgetContainerService {

    private ViewRenderService viewRenderService;
    private String path;

    @Override
    public void init() {
        super.init();
        if (path == null) {
            path = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WINT_OUTER_TEMPLATE_PATH, Constants.Defaults.WINT_OUTER_TEMPLATE_PATH);
        }
        if (new File(path).exists()) {
            log.warn("outer template directory:" + path + " is exists.");
            DefaultViewRenderService defaultViewRenderService = new DefaultViewRenderService();
            defaultViewRenderService.setServiceContext(serviceContext);
            defaultViewRenderService.setBasePath(path);
            defaultViewRenderService.setUseMacroLibrary(false);
            defaultViewRenderService.init();
            viewRenderService = defaultViewRenderService;
        }
    }

    @Override
    public OuterWidgetContainer createContainer(InnerFlowData flowData, Context context) {
        if (viewRenderService == null) {
            return null;
        }
        return new OuterWidgetContainer(viewRenderService, context);
    }

    public void setPath(String path) {
        this.path = path;
    }
}
