package wint.help.json.wrapper;

import wint.lang.convert.ConvertUtil;
import wint.lang.convert.converts.SmartDateConvert;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by songlihuang on 2020/6/4.
 */
public class DefaultJsonObject implements JsonObject {

    private Map<String, Object> stringObjectMap;

    public DefaultJsonObject(Map<String, Object> stringObjectMap) {
        this.stringObjectMap = stringObjectMap;
    }

    @Override
    public int getIntValue(String name) {
        Integer value = getInteger(name);
        if (value == null) {
            return 0;
        }
        return value;
    }

    @Override
    public Integer getInteger(String name) {
        Object o = stringObjectMap.get(name);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Integer.class, null);
    }

    @Override
    public long getLongValue(String name) {
        Long value = getLong(name);
        if (value == null) {
            return 0;
        }
        return value;
    }

    @Override
    public Long getLong(String name) {
        Object o = stringObjectMap.get(name);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Long.class, null);
    }

    @Override
    public String getString(String name) {
        Object o = stringObjectMap.get(name);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    @Override
    public boolean getBooleanValue(String name) {
        Boolean value = getBoolean(name);
        if (value == null) {
            return false;
        }
        return value;
    }

    @Override
    public Boolean getBoolean(String name) {
        Object o = stringObjectMap.get(name);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Boolean.class, null);
    }

    @Override
    public Date getDate(String name) {
        Object o = stringObjectMap.get(name);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Date.class, null);
    }

    @Override
    public JsonObject getObject(String name) {
        Object o = stringObjectMap.get(name);
        if (o == null) {
            return null;
        }
        if (o instanceof Map) {
            return new DefaultJsonObject((Map)o);
        }
        return null;
    }

    @Override
    public JsonList getList(String name) {
        Object o = stringObjectMap.get(name);
        if (o == null) {
            return null;
        }
        if (o instanceof List) {
            return new DefaultJsonList((List)o);
        }
        return null;
    }

    @Override
    public String toString() {
        return "DefaultJsonObject{" +
                "stringObjectMap=" + stringObjectMap +
                '}';
    }
}
