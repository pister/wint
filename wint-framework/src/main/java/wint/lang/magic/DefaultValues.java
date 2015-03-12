package wint.lang.magic;

import wint.lang.utils.ClassUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * User: huangsongli
 * Date: 15/3/12
 * Time: 下午1:23
 */
public class DefaultValues {

    private static Map<String, Object> defaultValues = new HashMap<String, Object>();

    static {
        defaultValues.put("boolean", false);
        defaultValues.put("Boolean", null);
        defaultValues.put("byte", (byte) 0);
        defaultValues.put("Byte", null);
        defaultValues.put("short", (short) 0);
        defaultValues.put("Short", null);
        defaultValues.put("int", 0);
        defaultValues.put("Integer", null);
        defaultValues.put("long", 0L);
        defaultValues.put("Long", null);
        defaultValues.put("float", (float) 0);
        defaultValues.put("Float", null);
        defaultValues.put("double", (double) 0);
        defaultValues.put("Double", null);
        defaultValues.put("String", null);
        defaultValues.put("string", null);
        defaultValues.put("Date", null);
        defaultValues.put("date", null);
    }

    public static Object getDefaultValue(String type) {
        return defaultValues.get(type);
    }

    public static Object getDefaultValue(Class targetClass) {
        return getDefaultValue(ClassUtil.getShortClassName(targetClass.getName()));
    }

}
