package wint.mvc.template.widget.outer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.lang.utils.FileUtil;
import wint.lang.utils.MapUtil;
import wint.mvc.template.*;
import wint.mvc.template.engine.TemplateEngine;
import wint.mvc.template.remote.HttpResourceTemplateRender;
import wint.mvc.template.remote.PathUtil;
import wint.mvc.view.Render;
import wint.mvc.view.ViewRenderService;

import java.io.File;
import java.util.Map;

/**
 * User: huangsongli
 * Date: 15/1/8
 * Time: 上午10:17
 */
public class OuterWidgetRender implements Render {

    private static final Logger log = LoggerFactory.getLogger(OuterWidgetRender.class);

    private Map<String, Object> contextValues = MapUtil.newHashMap();
    private Context context;
    private ViewRenderService viewRenderService;
    private String path;
    private OutWidgetParams params;

    public OuterWidgetRender(Context context, ViewRenderService viewRenderService, String path, OutWidgetParams params) {
        this.context = context;
        this.viewRenderService = viewRenderService;
        this.path = path;
        this.params = params;
    }

    @Override
    public String render() {
        Context widgetContext = new DefaultContext(context);
        widgetContext.putAll(contextValues);
        String extName = FileUtil.getFileExtension(path);
        TemplateEngine templateEngine = viewRenderService.getTemplateEngine(extName);
        TemplateRender templateRender;
        if (PathUtil.isRemotePath(params.getTemplateBasePath())) {
            HttpResourceTemplateRender httpResourceTemplateRender = new HttpResourceTemplateRender(path, templateEngine, widgetContext, params.getTemplateBasePath());
            httpResourceTemplateRender.setExpireInSeconds(params.getExpireInSeconds());
            httpResourceTemplateRender.setTempDir(params.getLocalTempBasePath());
            httpResourceTemplateRender.setRemoteFailUseCache(params.isRemoteFailUseCache());
            templateRender = httpResourceTemplateRender;
        } else {
            templateRender = new DefaultTemplateRender(path, templateEngine, widgetContext);
        }
        try {
            return templateRender.render();
        } catch (Exception e) {
            log.error("render out widget error", e);
            return "$outerWidgetRenderError$";
        }
    }

    public OuterWidgetRender addToContext(String name, Object o) {
        contextValues.put(name, o);
        return this;
    }
}
