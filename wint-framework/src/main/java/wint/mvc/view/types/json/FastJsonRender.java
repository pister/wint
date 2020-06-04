package wint.mvc.view.types.json;



public class FastJsonRender extends AbstractJsonRender {

    public FastJsonRender(String jsonRoot) {
        super(jsonRoot);
    }

    @Override
    protected String toJsonString(Object object) {
        throw new UnsupportedOperationException();
       // return JSONObject.toJSONString(object, SerializerFeature.WriteMapNullValue);
    }

}
