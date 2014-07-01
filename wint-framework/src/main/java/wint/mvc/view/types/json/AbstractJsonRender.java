package wint.mvc.view.types.json;

import wint.mvc.template.Context;

import java.io.IOException;
import java.io.Writer;

/**
 * User: huangsongli
 * Date: 14-7-1
 * Time: 下午8:05
 */
public abstract class AbstractJsonRender implements JsonRender {

    protected String jsonRoot;

    protected AbstractJsonRender(String jsonRoot) {
        this.jsonRoot = jsonRoot;
    }

    protected abstract String toJsonString(Object object);

    public void render(Context context, Writer writer) throws IOException {
        Object object = getObject(context, jsonRoot);
        writer.write(toJsonString(object));
    }

    protected Object getObject(Context context, String jsonRoot) {
        Object object = context.get(jsonRoot);
        if (object == null) {
            return context.getAll();
        } else {
            return object;
        }
    }

}
