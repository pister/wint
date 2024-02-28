package wint.core.config.property;

import java.util.Iterator;
import java.util.Map;

/**
 * @author songlihuang
 * @date 2024/2/28 09:28
 */
public interface PropertiesMap {

    boolean getBoolean(String name, boolean defaultValue);

    boolean getBoolean(String name);

    long getLong(String name);

    long getLong(String name, long defaultValue);

    int getInt(String name, int defaultValue);

    int getInt(String name);

    String getString(String name, String defaultValue);

    String getString(String name);

    Iterator<Map.Entry<String, Object>> iterator();

    boolean containsKey(String key);
}
