package wint.help.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import wint.help.json.wrapper.DefaultJsonList;
import wint.help.json.wrapper.DefaultJsonObject;
import wint.help.json.wrapper.JsonList;
import wint.help.json.wrapper.JsonObject;

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
            builder.disableHtmlEscaping();
            defaultGson = builder.create();
        }
        {
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
            builder.disableHtmlEscaping();
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

    public static JsonObject fromJsonStringObject(String s) {
        return new DefaultJsonObject((Map<String, Object>) defaultGson.fromJson(s, Map.class));
    }

    public static JsonList fromJsonStringList(String s) {
        return new DefaultJsonList((List<Object>)defaultGson.fromJson(s, List.class));
    }


}
