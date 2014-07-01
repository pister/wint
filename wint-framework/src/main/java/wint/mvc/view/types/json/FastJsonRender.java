package wint.mvc.view.types.json;

import com.alibaba.fastjson.JSONObject;

public class FastJsonRender extends AbstractJsonRender {

    public FastJsonRender(String jsonRoot) {
        super(jsonRoot);
    }

    @Override
    protected String toJsonString(Object object) {
        return JSONObject.toJSONString(object);
    }

}
