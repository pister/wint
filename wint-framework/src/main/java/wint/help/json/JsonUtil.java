package wint.help.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by songlihuang on 2020/6/4.
 */
public class JsonUtil {

    private static Gson defaultGson;

    private static Gson withNullGson;

    static {
        {
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
            defaultGson = builder.create();
        }
        {
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
            builder.serializeNulls();
            withNullGson = builder.create();
        }
    }

    public static String toJsonString(Object object) {
        return defaultGson.toJson(object);
    }

    public static String toJsonStringWithNull(Object object) {
        return withNullGson.toJson(object);
    }

    public static <T> T fromJsonString(String s, Class<T> classOfT) {
        return defaultGson.fromJson(s, classOfT);
    }

    public static Map<String, Object> fromJsonStringAsMap(String s) {
        return (Map<String, Object>) defaultGson.fromJson(s, Map.class);
    }

    public static List<Object> fromJsonStringAsList(String s) {
        return (List<Object>)defaultGson.fromJson(s, List.class);
    }


}
