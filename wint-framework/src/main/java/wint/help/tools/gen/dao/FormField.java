package wint.help.tools.gen.dao;

import wint.lang.utils.MapUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * User: longyi
 * Date: 13-12-21
 * Time: 下午2:00
 */
public class FormField {

    private static Map<Class<?>, String> class2type = MapUtil.newHashMap();

    static {
        class2type.put(Integer.class, "int");
        class2type.put(Integer.TYPE, "int");
        class2type.put(Short.class, "int");
        class2type.put(Short.TYPE, "int");
        class2type.put(Long.class, "long");
        class2type.put(Long.TYPE, "long");
        class2type.put(Float.class, "number");
        class2type.put(Float.TYPE, "number");
        class2type.put(Double.class, "number");
        class2type.put(Double.TYPE, "number");
        class2type.put(String.class, "string");
        class2type.put(Date.class, "date");
        class2type.put(Timestamp.class, "date");
    }

    private String name;

    private String type;

    public FormField(String name, Class<?> javaType) {
        this.name = name;
        String type = class2type.get(javaType);
        if (type == null) {
            type = "string";
        }
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return "#errorMessageDiv($!form."+ name +".message)";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
