package wint.sessionx.serialize;

import wint.core.config.Constants;
import wint.lang.WintException;
import wint.lang.convert.ConvertUtil;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.UrlUtil;
import wint.sessionx.util.WintInnerHackBase64;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Map;

public class StringSerializeService implements SerializeService {

    static final String TYPE_DATA_SEP = "|";
    private static final Class<?>[] DATE_TYPE_CONTOR_ARG_TYPES = new Class<?>[]{Long.TYPE};
    private static final String NULL_CLASS_NAME = "<null>";
    private static final String URL_ENCODE_CHARSET = Constants.Defaults.CHARSET_ENCODING;
    private Map<Class<?>, String> type2name = MapUtil.newHashMap();
    private Map<String, Class<?>> name2type = MapUtil.newHashMap();
    private Map<Class<?>, Object> defaultValues = MapUtil.newHashMap();
    private SerializeService objectSerializeService = new JavaSerializeService();

    public StringSerializeService() {
        init();
    }

    private void init() {
        register(Void.TYPE, "v");
        register(Void.class, "V");
        register(Boolean.TYPE, "z");
        register(Boolean.class, "Z");
        register(Byte.TYPE, "b");
        register(Byte.class, "B");
        register(Character.TYPE, "c");
        register(Character.class, "C");
        register(Short.TYPE, "s");
        register(Short.class, "S");
        register(Integer.TYPE, "i");
        register(Integer.class, "I");
        register(Long.TYPE, "j");
        register(Long.class, "J");
        register(Float.TYPE, "f");
        register(Float.class, "F");
        register(Double.TYPE, "d");
        register(Double.class, "D");
        register(String.class, "str");

        defaultValues.put(Void.TYPE, null);
        defaultValues.put(Void.class, null);
        defaultValues.put(Boolean.TYPE, false);
        defaultValues.put(Boolean.class, null);
        defaultValues.put(Byte.TYPE, (byte) 0);
        defaultValues.put(Byte.class, null);
        defaultValues.put(Character.TYPE, (char) 0);
        defaultValues.put(Character.class, null);
        defaultValues.put(Short.TYPE, (short) 0);
        defaultValues.put(Short.class, null);
        defaultValues.put(Integer.TYPE, 0);
        defaultValues.put(Integer.class, null);
        defaultValues.put(Long.TYPE, 0L);
        defaultValues.put(Long.class, null);
        defaultValues.put(Float.TYPE, (float) 0);
        defaultValues.put(Float.class, null);
        defaultValues.put(Double.TYPE, (double) 0);
        defaultValues.put(Double.class, null);
        defaultValues.put(String.class, null);
    }

    private void register(Class<?> clazz, String name) {
        name2type.put(name, clazz);
        type2name.put(clazz, name);
    }

    public Object serialize(Object input) {
        if (input == null) {
            return NULL_CLASS_NAME + TYPE_DATA_SEP + "null";
        }
        Class<?> type = input.getClass();
        String name = getName(type);
        String data;
        if (type2name.containsKey(type)) {
            data = UrlUtil.encode(input.toString(), URL_ENCODE_CHARSET);
        } else {
            data = serializeObject(input);
        }
        return name + TYPE_DATA_SEP + data;
    }

    private String serializeObject(Object input) {
        // 日期类型特殊处理
        if (input instanceof Date) {
            return Long.toHexString(((Date) input).getTime());
        }
        byte[] bytes = (byte[]) objectSerializeService.serialize(input);
        return new String(WintInnerHackBase64.encodeBase64(bytes));
    }

    private Object unserializeObject(Class<?> type, String data) {
        try {
            // 日期类型特殊处理
            if (Date.class.isAssignableFrom(type)) {
                long ts = Long.parseLong(data, 16);
                Constructor<?> constructor = type.getConstructor(DATE_TYPE_CONTOR_ARG_TYPES);
                return constructor.newInstance(new Object[]{ts});
            }
            byte[] rawBytes = WintInnerHackBase64.decodeBase64(data.getBytes());
            return objectSerializeService.unserialize(rawBytes);
        } catch (Exception e) {
            throw new WintException(e);
        }
    }

    private String getName(Class<?> type) {
        String name = type2name.get(type);
        if (name == null) {
            return type.getName();
        }
        return name;
    }

    public Object unserialize(Object src) {
        if (src == null) {
            return null;
        }
        String s = src.toString();
        int pos = s.indexOf(TYPE_DATA_SEP);
        if (pos < 0) {
            return null;
        }
        String typeName = StringUtil.getFirstBefore(s, TYPE_DATA_SEP);
        String data = StringUtil.getFirstAfter(s, TYPE_DATA_SEP);
        Class<?> type = name2type.get(typeName);
        Object defaultValue = defaultValues.get(type);
        if (type != null) {
            data = UrlUtil.decode(data.toString(), URL_ENCODE_CHARSET);
            return ConvertUtil.convertTo(data, type, defaultValue);
        } else {
            if (NULL_CLASS_NAME.equals(typeName)) {
                return null;
            }
            type = ClassUtil.forName(typeName);
            return unserializeObject(type, data);
        }
    }

}
