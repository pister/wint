package wint.lang.utils;

import wint.lang.magic.MagicObject;
import wint.lang.magic.Property;

import java.lang.reflect.Array;
import java.time.temporal.Temporal;
import java.util.*;

public class ObjectUtil {
	
	public static boolean equals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		return o1.equals(o2);
	}

    public static String toString(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    public static Object walkProperties(Object input, PropertyValueWalker propertyValueWalker) {
		if (input == null) {
			return propertyValueWalker.filter(input);
		}
        if (isSimpleObject(input)) {
		    return propertyValueWalker.filter(input);
        }
        if (input instanceof Map) {
		    Map<Object, Object> params = (Map<Object, Object>)input;
		    Map<Object, Object> newMap = MapUtil.newHashMap();
		    for (Map.Entry<Object, Object> entry : params.entrySet()) {
                newMap.put(entry.getKey(), walkProperties(entry.getValue(), propertyValueWalker));
            }
            return newMap;
        }
        if (input instanceof Collection) {
            Collection newCollection;
		    if (input instanceof List) {
                newCollection = CollectionUtil.newArrayList();
            } else if (input instanceof Set) {
		        newCollection = CollectionUtil.newHashSet();
            } else {
                newCollection = CollectionUtil.newArrayList();
            }
            for (Object o : (Collection)input) {
                newCollection.add(walkProperties(o, propertyValueWalker));
            }
            return newCollection;
        }
        if (input.getClass().isArray()) {
            Object newArray = Array.newInstance(input.getClass().getComponentType(), Array.getLength(input));
            for (int i = 0, len = Array.getLength(input); i < len; ++i) {
                Object item = Array.get(input, i);
                Array.set(newArray, i, walkProperties(item, propertyValueWalker));
            }
            return newArray;
        }
        MagicObject magicObject = MagicObject.wrap(input);
        Map<String, Property> propertyMap = magicObject.getMagicClass().getReadAndWritableProperties();
        for (Map.Entry<String, Property> entry : propertyMap.entrySet()) {
            Property property = entry.getValue();
            if (!property.isWritable()) {
                continue;
            }
            Object value = property.getValue(input);
            Object newValue = walkProperties(value, propertyValueWalker);
            try {
                property.setValue(input, newValue);
            } catch (RuntimeException e) {
                throw e;
            }
        }

		return input;
	}

	private static boolean isSimpleObject(Object input) {
        Class<?> clazz = input.getClass();
        if (ClassUtil.isSimpleType(clazz)) {
            return true;
        }
        if (input instanceof Date) {
            return true;
        }
        if (input instanceof CharSequence) {
            return true;
        }
        if (input instanceof Temporal) {
            return true;
        }
        return false;
    }

	public interface PropertyValueWalker {
		Object filter(Object value);
	}

}
