package wint.help.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by songlihuang on 2020/6/4.
 */
public class GsonUtil {

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

    public static String toJsonString(Object object, Type t) {
        return defaultGson.toJson(object, t);
    }

    public static <T> T formJsonString(String s, Class<T> classOfT) {
        return defaultGson.fromJson(s, classOfT);
    }


}
