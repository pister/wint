package wint.mvc.template.widget.outer;

import wint.mvc.template.Context;
import wint.mvc.view.Render;
import wint.mvc.view.ViewRenderService;

/**
 * User: huangsongli
 * Date: 15/1/8
 * Time: 上午10:23
 */
public class OuterWidgetContainer implements Render {

    private ViewRenderService viewRenderService;
    private Context context;

    public OuterWidgetContainer(ViewRenderService viewRenderService, Context context) {
        this.viewRenderService = viewRenderService;
        this.context = context;
    }

    @Override
    public String render() {
        return "$OuterWidget$";
    }

    public OuterWidgetRender setTemplate(String templateName) {
        return new OuterWidgetRender(context, viewRenderService, templateName);
    }
}
