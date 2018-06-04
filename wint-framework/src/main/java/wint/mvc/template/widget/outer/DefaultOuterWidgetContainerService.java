package wint.mvc.template.widget.outer;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.Context;
import wint.mvc.template.remote.PathUtil;
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
    private String templateBasePath;

    private OutWidgetParams params;

    @Override
    public void init() {
        super.init();
        if (templateBasePath == null) {
            templateBasePath = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WINT_OUTER_TEMPLATE_PATH, Constants.Defaults.WINT_OUTER_TEMPLATE_PATH);
        }
        String localTempBasePath = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WINT_OUTER_TEMPLATE_TEMP_PATH, Constants.Defaults.WINT_OUTER_TEMPLATE_TEMP_PATH);
        int expireInSeconds = serviceContext.getConfiguration().getProperties().getInt(Constants.PropertyKeys.WINT_OUTER_TEMPLATE_EXPIRE_SECONDS, Constants.Defaults.WINT_OUTER_TEMPLATE_EXPIRE_SECONDS);

        params = new OutWidgetParams();
        params.setTemplateBasePath(templateBasePath);
        params.setLocalTempBasePath(localTempBasePath);
        params.setExpireInSeconds(expireInSeconds);

        if (StringUtil.isEmpty(templateBasePath)) {
            log.warn("outer template basePath is not set.");
        } else {
            DefaultViewRenderService defaultViewRenderService = new DefaultViewRenderService();
            defaultViewRenderService.setServiceContext(serviceContext);
            defaultViewRenderService.setUseMacroLibrary(false);
            log.warn("outer template basePath: " + templateBasePath + " ... ");
            if (PathUtil.isRemotePath(templateBasePath)) {
                log.warn("outer template basePath is remote path.");
                log.warn("local temp base path is: " + localTempBasePath);
                File tempBaseFile = new File(localTempBasePath);
                if (!tempBaseFile.exists()) {
                    tempBaseFile.mkdirs();
                }
                defaultViewRenderService.setBasePath(localTempBasePath);
            } else {
                defaultViewRenderService.setBasePath(templateBasePath);
            }
            defaultViewRenderService.init();
            viewRenderService = defaultViewRenderService;
        }
    }


    @Override
    public OuterWidgetContainer createContainer(InnerFlowData flowData, Context context) {
        if (viewRenderService == null) {
            return null;
        }
        return new OuterWidgetContainer(viewRenderService, context, params);
    }

    /**
     * @param path
     * @deprecated use setTemplateBasePath instead!
     */
    public void setPath(String path) {
        setTemplateBasePath(path);
    }

    public void setTemplateBasePath(String templateBasePath) {
        this.templateBasePath = templateBasePath;
    }
}
