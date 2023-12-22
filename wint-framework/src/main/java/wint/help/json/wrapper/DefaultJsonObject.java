package wint.help.json.wrapper;

import wint.lang.convert.ConvertUtil;
import wint.lang.utils.LocalDateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
    public LocalDateTime getLocalDateTime(String name) {
        Object o = stringObjectMap.get(name);
        if (o instanceof LocalDateTime) {
            return (LocalDateTime) o;
        }
        if (o == null) {
            return null;
        }
        return LocalDateTimeUtil.parseDateTime(o.toString());
    }

    @Override
    public LocalDate getLocalDate(String name) {
        Object o = stringObjectMap.get(name);
        if (o instanceof LocalDate) {
            return (LocalDate) o;
        }
        if (o == null) {
            return null;
        }
        return LocalDateTimeUtil.parseDate(o.toString());
    }

    @Override
    public LocalTime getLocalTime(String name) {
        Object o = stringObjectMap.get(name);
        if (o instanceof LocalTime) {
            return (LocalTime) o;
        }
        if (o == null) {
            return null;
        }
        return LocalDateTimeUtil.parseTime(o.toString());
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

    @Override
    public int size() {
        return stringObjectMap.size();
    }

    @Override
    public boolean isEmpty() {
        return stringObjectMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return stringObjectMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return stringObjectMap.containsKey(value);
    }

    @Override
    public Object get(Object key) {
        return stringObjectMap.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        return stringObjectMap.keySet();
    }

    @Override
    public Collection<Object> values() {
        return stringObjectMap.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return stringObjectMap.entrySet();
    }
}
