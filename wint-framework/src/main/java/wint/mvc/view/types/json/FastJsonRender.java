package wint.mvc.view.types.json;

import java.io.Writer;

import wint.lang.WintException;
import wint.mvc.template.Context;

import com.alibaba.fastjson.JSONObject;

public class FastJsonRender implements JsonRender {

	public void render(Context context, Writer writer) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.putAll(context.getAll());
			writer.write(jsonObject.toJSONString());
		} catch (Exception e) {
			throw new WintException(e);
		}
	}


}
