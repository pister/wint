package wint.lang.enums;

import wint.lang.WintException;
import wint.lang.magic.MagicObject;
import wint.lang.magic.Transformer;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.ObjectUtil;
import wint.lang.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 枚举工具类
 * User: longyi
 * Date: 14-1-20
 * Time: 下午5:18
 */
public class EnumsUtil {

    private static Map<Class<?>, HashMap<String, Enum<?>>> cache = new ConcurrentHashMap<Class<?>, HashMap<String, Enum<?>>>();

    private static String makeKey(String fieldName, Object fieldValue) {
        return fieldName + "-" + fieldValue;
    }

    private static Map<String, Method> getterMethods(Class<?> enumClass) {
        Method[] methods = enumClass.getMethods();
        Map<String, Method> named2Method = new HashMap<String, Method>();
        for (Method method : methods) {
            Object retType = method.getReturnType();
            if (retType == null || retType.equals(Void.class)) {
                continue;
            }
            Class<?>[] params = method.getParameterTypes();
            if (params != null && params.length > 0) {
                continue;
            }
            String methodName = method.getName();
            if (methodName.startsWith("get")) {
                String name = methodName.substring(3);
                name = StringUtil.lowercaseFirstLetter(name);
                named2Method.put(name, method);
            } else if (retType.equals(Boolean.class) || retType.equals(Boolean.TYPE)) {
                if (methodName.startsWith("is")) {
                    String name = methodName.substring(2);
                    name = StringUtil.lowercaseFirstLetter(name);
                    named2Method.put(name, method);
                }
            }
        }
        return named2Method;
    }

    private static HashMap<String, Enum<?>> toEnumMaps(Class<?> enumClass, Enum<?>[] values) {
        Map<String, Method> namedMethods = getterMethods(enumClass);
        HashMap<String, Enum<?>> fieldsEnum = new HashMap<String, Enum<?>>();
        for (Enum<?> enumObject : values) {
            for (Map.Entry<String, Method> methodEntry : namedMethods.entrySet()) {
                try {
                    String fieldName = methodEntry.getKey();
                    Method method = methodEntry.getValue();
                    Object value = method.invoke(enumObject);
                    String key = makeKey(fieldName, value);
                    fieldsEnum.put(key, enumObject);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return fieldsEnum;
    }

    /**
     * 通过枚举类的成员名称和成员值获取枚举，如果没有则返回null
     *
     * @param values
     * @param fieldName
     * @param fieldValue
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> Enum<E> findEnumByFieldValue(Enum<E>[] values, String fieldName, Object fieldValue) {
        if (values == null || values.length == 0) {
            return null;
        }
        Class<E> enumClass = values[0].getDeclaringClass();
        HashMap<String, Enum<?>> enumMap = cache.get(enumClass);
        String key = makeKey(fieldName, fieldValue);
        if (enumMap == null) {
            enumMap = toEnumMaps(enumClass, values);
            cache.put(enumClass, enumMap);
        }
        return (Enum<E>) enumMap.get(key);
    }

    /**
     * 获取成员为value对应值的枚举，如果没有则返回null
     *
     * @param values
     * @param fieldValue
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> Enum<E> findEnumByValue(Enum<E>[] values, Object fieldValue) {
        return findEnumByFieldValue(values, "value", fieldValue);
    }

    /**
     * 获取enum所有值列表
     *
     * @param values
     * @param fieldname
     * @return
     */
    public static List<Object> enumValues(Enum[] values, String fieldname) {
        if (values == null || values.length == 0) {
            return CollectionUtil.newArrayList(0);
        }
        Class enumClass = values[0].getDeclaringClass();
        Map<String, Method> namedMethods = getterMethods(enumClass);
        Method method = namedMethods.get(fieldname);
        List<Object> ret = new ArrayList<Object>(values.length);
        for (Enum value : values) {
            try {
                ret.add(method.invoke(value, null));
            } catch (Exception e) {
                throw new RuntimeException(e);

            }
        }
        return ret;
    }

    /**
     * 通过一个类目类名，返回其枚举值，相当于调用 <code>Enum.values()</code> 方法，
     *
     * @param enumClass
     * @return
     * @throws WintException 如果目标不是一个枚举类，则抛出异常
     */
    public static Enum[] enums(String enumClass) {
        Class<?> clazz = ClassUtil.forName(enumClass);
        if (!clazz.isEnum()) {
            throw new WintException(enumClass + " is not an Enum!");
        }
        try {
            Method method = clazz.getMethod("values");
            return (Enum[]) method.invoke(clazz);
        } catch (Exception e) {
            throw new WintException(e);
        }
    }

    private static Enum[] filterEnums(Enum[] enums, EnumFilter filter) {
        if (enums == null || enums.length == 0) {
            return enums;
        }
        List<Enum> ret = CollectionUtil.newArrayList(enums.length);
        for (Enum e : enums) {
            if (filter.accept(e)) {
                ret.add(e);
            }
        }
        return ret.toArray(new Enum[0]);
    }

    static Enum[] enumsExcludes(String enumClass, final String valueName, Object... values) {
        Enum[] enums = enums(enumClass);
        if (values == null || values.length == 0) {
            return enums;
        }
        NamedArrayFilter enumFilter = new NamedArrayFilter() {
            @Override
            protected boolean acceptValue(String value) {
                return !valuesSet.contains(value);
            }
        };
        enumFilter.initWithData(valueName, values);
        return filterEnums(enums, enumFilter);
    }

    public static Enum[] enumsExcludeValues(String enumClass, Object... values) {
        return enumsExcludes(enumClass, "value", values);
    }

    public static Enum[] enumsIncludes(String enumClass, final String valueName, Object... values) {
        Enum[] enums = enums(enumClass);
        if (values == null || values.length == 0) {
            return enums;
        }
        NamedArrayFilter enumFilter = new NamedArrayFilter() {
            @Override
            protected boolean acceptValue(String value) {
                return valuesSet.contains(value);
            }
        };
        enumFilter.initWithData(valueName, values);
        return filterEnums(enums, enumFilter);
    }

    public static Enum[] enumsIncludeValues(String enumClass, String valueName, Object... values) {
        return enumsIncludes(enumClass, "value", values);
    }


    protected static interface EnumFilter {
        boolean accept(Enum e);
    }

    protected abstract static class NamedArrayFilter implements EnumFilter {

        protected Set<String> valuesSet;
        private String valueName;

        @Override
        public boolean accept(Enum e) {
            MagicObject o = MagicObject.wrap(e);
            Object v = o.getPropertyValue(valueName);
            if (acceptValue(ObjectUtil.toString(v))) {
                return true;
            }
            return false;
        }

        protected abstract boolean acceptValue(String value);

        public void initWithData(String valueName, Object... values) {
            List<String> stringValues = CollectionUtil.transformList(Arrays.asList(values), new Transformer<Object, String>() {
                @Override
                public String transform(Object object) {
                    if (object == null) {
                        return null;
                    }
                    return object.toString();
                }
            });
            this.valuesSet = new HashSet<String>(stringValues);
            this.valueName = valueName;
        }

    }

    public static void main(String[] args) {
        Enum[] enums = enumsExcludeValues("wint.help.tools.gen.dao.OptionEnum", "1");
        for (Enum e : enums) {
            System.out.println(e);
        }
    }


}
