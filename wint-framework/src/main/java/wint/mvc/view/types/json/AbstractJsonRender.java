package wint.mvc.view.types.json;

import wint.lang.utils.EscapeUtil;
import wint.lang.utils.ObjectUtil;
import wint.lang.utils.ObjectUtil.PropertyValueWalker;
import wint.lang.utils.SecurityUtil;
import wint.mvc.template.Context;
import wint.mvc.view.Render;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

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
            return filterObjectValues(context.getAll());
        } else {
            return filterObjectValues(object);
        }
    }

    protected Object filterObjectValues(Object object) {
        return ObjectUtil.walkProperties(object, new PropertyValueWalker() {

            @Override
            public Object filter(Object value) {
                if (value instanceof Render) {
                    return ((Render)value).render();
                }
                if (value instanceof String) {
                    String stringValue = (String)value;
                    if (SecurityUtil.isRawString(stringValue)) {
                        return SecurityUtil.tryExtractRawString(stringValue);
                    }
                    return EscapeUtil.escapeHtmlSimple(stringValue);
                }
                return value;
            }
        });
    }





}
