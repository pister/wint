package wint.mvc.view.types.json;

import wint.help.json.JsonUtil;

/**
 * Created by songlihuang on 2020/6/4.
 */
public class GsonJsonRender extends AbstractJsonRender {

    public GsonJsonRender(String jsonRoot) {
        super(jsonRoot);
    }

    @Override
    protected String toJsonString(Object object) {
        return JsonUtil.toJsonStringWithNull(object);
    }
}
