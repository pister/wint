package wint.core.config.property;

import wint.lang.magic.MagicMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author songlihuang
 * @date 2024/2/28 09:30
 */
public class MagicPropertiesMap implements PropertiesMap{

    private static final String WINT_ENV_PROPERTY_NAME_PREFIX = "wint.";

    private MagicMap magicMap;

    public MagicPropertiesMap(MagicMap initMap) {
        MagicMap magicMap = MagicMap.newMagicMap();
        magicMap.putAll(initMap);
        magicMap.putAll(initEnvProperties());
        this.magicMap = magicMap;
    }

    private MagicMap initEnvProperties() {
        MagicMap envMap = MagicMap.newMagicMap();
        Properties properties = System.getProperties();
        for (Map.Entry entry :properties.entrySet()) {
            String name = entry.getKey().toString();
            if (!name.startsWith(WINT_ENV_PROPERTY_NAME_PREFIX)) {
                continue;
            }
            envMap.put(name, entry.getValue().toString());
        }
        return envMap;
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        return magicMap.getBoolean(name, defaultValue);
    }

    @Override
    public boolean getBoolean(String name) {
        return magicMap.getBoolean(name);
    }

    @Override
    public long getLong(String name) {
        return magicMap.getLong(name);
    }

    @Override
    public long getLong(String name, long defaultValue) {
        return magicMap.getLong(name, defaultValue);
    }

    @Override
    public int getInt(String name, int defaultValue) {
        return magicMap.getInt(name, defaultValue);
    }

    @Override
    public int getInt(String name) {
        return magicMap.getInt(name);
    }

    @Override
    public String getString(String name, String defaultValue) {
        return magicMap.getString(name, defaultValue);
    }

    @Override
    public String getString(String name) {
        return magicMap.getString(name);
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return magicMap.entrySet().iterator();
    }

    @Override
    public boolean containsKey(String key) {
        return magicMap.containsKey(key);
    }
}
