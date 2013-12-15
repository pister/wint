package wint.mvc.view.types.json;

import wint.lang.WintException;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicObject;
import wint.mvc.template.Context;

import java.io.Writer;
import java.util.Map;

public class FastJsonRender implements JsonRender {

    public void render(Context context, Writer writer) {
        try {
            MagicClass mc = MagicClass.forName("com.alibaba.fastjson.JSONObject");
            MagicObject object = mc.newInstance();
            object.invoke("putAll", new Class<?>[]{Map.class}, new Object[]{context.getAll()});
            String s = (String) object.invoke("toJSONString");
            //	JSONObject jsonObject = new JSONObject();
            //	jsonObject.putAll(context.getAll());
            writer.write(s);
        } catch (Exception e) {
            throw new WintException(e);
        }
    }


}
