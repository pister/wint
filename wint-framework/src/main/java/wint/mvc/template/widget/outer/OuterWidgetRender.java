package wint.mvc.template.widget.outer;

import wint.lang.utils.FileUtil;
import wint.lang.utils.MapUtil;
import wint.mvc.template.*;
import wint.mvc.template.engine.TemplateEngine;
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

    private Map<String, Object> contextValues = MapUtil.newHashMap();
    private Context context;
    private ViewRenderService viewRenderService;
    private String path;

    public OuterWidgetRender(Context context, ViewRenderService viewRenderService, String path) {
        this.context = context;
        this.viewRenderService = viewRenderService;
        this.path = path;
    }

    @Override
    public String render() {
        Context widgetContext = new DefaultContext(context);
        widgetContext.putAll(contextValues);
        String extName = FileUtil.getFileExtension(path);
        TemplateEngine templateEngine = viewRenderService.getTemplateEngine(extName);
        DefaultTemplateRender templateRender = new DefaultTemplateRender(path, templateEngine, context);
        return templateRender.render();
    }

    public OuterWidgetRender addToContext(String name, Object o) {
        contextValues.put(name, o);
        return this;
    }
}
