package wint.json;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import wint.lang.utils.MapUtil;

import java.util.Map;

/**
 * Created by songlihuang on 2018/6/11.
 */
public class FastJsonEscapeTest extends TestCase {

    public void test0() {
        Map<String, Object> object = MapUtil.newHashMap();
        object.put("name", "pister");
        object.put("date", "2016/10/21");
        String s = JSONObject.toJSONString(object);
        System.out.println(s);
    }

}
