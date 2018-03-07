package wint.mvc.view.types;

import wint.core.config.Constants;
import wint.core.service.env.Environment;
import wint.lang.exceptions.FlowDataException;
import wint.lang.exceptions.ResourceException;
import wint.lang.magic.MagicMap;
import wint.lang.utils.StringUtil;
import wint.lang.utils.TargetUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.flow.StatusCodes;
import wint.mvc.template.Context;
import wint.mvc.template.DefaultContext;
import wint.mvc.template.LoadTemplateService;
import wint.mvc.template.TemplateRender;

import java.io.IOException;
import java.io.Writer;

/**
 * @author pister 2012-3-4 06:53:24
 */
public class TemplateViewRender extends AbstractViewRender {

    private LoadTemplateService loadTemplateService;

    private String layoutName;

    private String pageContentName;

    private Environment environment;

    public String getViewType() {
        return ViewTypes.TEMPLATE_VIEW_TYPE;
    }

    @Override
    public void init() {
        super.init();
        loadTemplateService = serviceContext.getService(LoadTemplateService.class);
        MagicMap properties = serviceContext.getConfiguration().getProperties();
        layoutName = properties.getString(Constants.PropertyKeys.TEMPLATE_LAYOUT, Constants.Defaults.TEMPLATE_LAYOUT);
        pageContentName = properties.getString(Constants.PropertyKeys.PAGE_CONTENT_NAME, Constants.Defaults.PAGE_CONTENT_NAME);
        environment = serviceContext.getConfiguration().getEnvironment();
    }

    public void render(Context context, InnerFlowData flowData, String target, String moduleType) {
        Context innerContext = flowData.getInnerContext();
        Context targetContext;
        if (innerContext != null) {
            targetContext = new DefaultContext(innerContext);
            targetContext.putContext(context);
        } else {
            targetContext = context;
        }

        target = TargetUtil.normalizeTarget(target);
        TemplateRender page = loadTemplateService.loadTemplate(target, targetContext, moduleType);
        if (page == null) {
            if (onResourceNotFound(flowData, target)) {
                return;
            } else {
                throw new ResourceException("can not find page " + target);
            }
        }
        String pageContent = page.render();
        Context layoutContext = targetContext.copyMe();

        String layoutTarget = flowData.getLayout();
        TemplateRender layout;
        if (!StringUtil.isEmpty(layoutTarget)) {
            layout = loadTemplateService.loadTemplate(layoutTarget, layoutContext, layoutName);
        } else {
            layout = loadTemplateService.loadTemplate(target, layoutContext, layoutName);
        }
        String content;
        if (layout != null) {
            layoutContext.put(pageContentName, pageContent);
            content = layout.render();
        } else {
            content = pageContent;
        }
        writeContent(flowData.getWriter(), content);
    }

    protected boolean onResourceNotFound(InnerFlowData flowData, String target) {
        if (environment.isSupportDev()) {
            flowData.setStatusCode(StatusCodes.SC_NOT_FOUND, "can not find target:" + target);
        } else {
            flowData.setStatusCode(StatusCodes.SC_NOT_FOUND);
        }
        return true;
    }

    private void writeContent(Writer writer, String content) {
        try {
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            throw new FlowDataException(e);
        }
    }

}
