package wint.help.json.wrapper;

import wint.lang.convert.ConvertUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by songlihuang on 2020/6/4.
 */
public class DefaultJsonList implements JsonList {

    private List<Object> objectList;

    public DefaultJsonList(List<Object> objectList) {
        this.objectList = objectList;
    }

    @Override
    public int getIntValue(int index) {
        Integer value = getInteger(index);
        if (value == null) {
            return 0;
        }
        return value;
    }

    @Override
    public Integer getInteger(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Integer.class, null);
    }

    @Override
    public long getLongValue(int index) {
        Long value = getLong(index);
        if (value == null) {
            return 0;
        }
        return value;
    }

    @Override
    public Long getLong(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Long.class, null);
    }

    @Override
    public String getString(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    @Override
    public boolean getBooleanValue(int index) {
        Boolean value = getBoolean(index);
        if (value == null) {
            return false;
        }
        return value;
    }

    @Override
    public Boolean getBoolean(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Boolean.class, null);
    }

    @Override
    public Date getDate(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Date.class, null);
    }

    @Override
    public JsonObject getObject(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        if (o instanceof Map) {
            return new DefaultJsonObject((Map)o);
        }
        return null;
    }


    @Override
    public JsonList getList(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        if (o instanceof List) {
            return new DefaultJsonList((List)o);
        }
        return null;
    }

    @Override
    public int getLength() {
        return objectList.size();
    }

    @Override
    public String toString() {
        return "DefaultJsonList{" +
                "objectList=" + objectList +
                '}';
    }
}
