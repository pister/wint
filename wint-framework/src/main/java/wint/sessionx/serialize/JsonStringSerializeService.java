package wint.sessionx.serialize;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午5:11
 */
public class JsonStringSerializeService implements SerializeService {

    @Override
    public Object serialize(Object input) {
        if (input == null) {
            return null;
        }
        return JSONObject.toJSONString(input, SerializerFeature.WriteClassName);
    }

    @Override
    public Object unserialize(Object src) {
        if (src == null) {
            return null;
        }
        return JSONObject.parse((String)src);
    }
}
