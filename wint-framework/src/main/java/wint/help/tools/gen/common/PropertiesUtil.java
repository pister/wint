package wint.help.tools.gen.common;

import wint.lang.magic.MagicClass;
import wint.lang.magic.Property;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by songlihuang on 2022/11/18.
 */
public class PropertiesUtil {

    public static Map<String, Property> getProperties(Class<?> clazz) {
        Map<String, Property> propertyMap = MagicClass.wrap(clazz).getReadAndWritableProperties();
        Map<String, Property> ret = new LinkedHashMap<>();
        Class<?> targetClass = clazz;
        while (true) {
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                Property property = propertyMap.get(field.getName());
                if (property == null) {
                    continue;
                }
                ret.put(field.getName(), property);
            }
            targetClass = targetClass.getSuperclass();
            if (targetClass == Object.class) {
                break;
            }
        }
        return ret;
    }
}

