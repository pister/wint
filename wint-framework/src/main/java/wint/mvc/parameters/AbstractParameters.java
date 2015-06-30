package wint.mvc.parameters;

import wint.lang.convert.ConvertUtil;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicObject;
import wint.lang.magic.Property;
import wint.lang.utils.StringUtil;
import wint.mvc.form.fileupload.UploadFile;

import java.lang.reflect.Array;
import java.util.*;

public abstract class AbstractParameters implements Parameters {
    @Override
    public Integer getIntX(String name) {
        String stringValue = getString(name, null);
        if (StringUtil.isEmpty(stringValue)) {
            return null;
        }
        try {
            return Integer.valueOf(stringValue);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Long getLongX(String name) {
        String stringValue = getString(name, null);
        if (StringUtil.isEmpty(stringValue)) {
            return null;
        }
        try {
            return Long.valueOf(stringValue);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Float getFloatX(String name) {
        String stringValue = getString(name, null);
        if (StringUtil.isEmpty(stringValue)) {
            return null;
        }
        try {
            return Float.valueOf(stringValue);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Double getDoubleX(String name) {
        String stringValue = getString(name, null);
        if (StringUtil.isEmpty(stringValue)) {
            return null;
        }
        try {
            return Double.valueOf(stringValue);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    public Date getDate(String name) {
        return getDate(name, (Date) null);
    }

    public Date getDate(String name, String format) {
        return getDate(name, format, null);
    }

    public double getDouble(String name) {
        return getDouble(name, 0.0);
    }

    public double[] getDoubleArray(String name) {
        return getDoubleArray(name, null);
    }

    public float getFloat(String name) {
        return getFloat(name, 0.0f);
    }

    public float[] getFloatArray(String name) {
        return getFloatArray(name, null);
    }

    public int getInt(String name) {
        return getInt(name, 0);
    }

    public int[] getIntArray(String name) {
        return getIntArray(name, null);
    }

    public long getLong(String name) {
        return getLong(name, 0L);
    }

    public long[] getLongArray(String name) {
        return getLongArray(name, null);
    }

    public String getString(String name) {
        return getString(name, null);
    }

    public String[] getStringArray(String name) {
        return getStringArray(name, null);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return ConvertUtil.toBoolean(getString(name), defaultValue);
    }

    public Date getDate(String name, Date defaultDate) {
        return ConvertUtil.toDate(getString(name), defaultDate);
    }

    public Date getDate(String name, String format, Date defaultDate) {
        return ConvertUtil.toDate(getString(name), format, defaultDate);
    }

    public double getDouble(String name, double defaultValue) {
        return ConvertUtil.toDouble(getString(name), defaultValue);
    }

    public double[] getDoubleArray(String name, double[] defaultArray) {
        return (double[])separateToArray(Double.TYPE, name, DEFAULT_SEPARATOR, defaultArray);
    }

    public float getFloat(String name, float defaultValue) {
        return ConvertUtil.toFloat(getString(name), defaultValue);
    }

    public float[] getFloatArray(String name, float[] defaultArray) {
        return (float[])separateToArray(Float.TYPE, name, DEFAULT_SEPARATOR, defaultArray);
    }

    public int getInt(String name, int defaultValue) {
        return ConvertUtil.toInt(getString(name), defaultValue);
    }

    public int[] getIntArray(String name, int[] defaultArray) {
        return (int[])separateToArray(Integer.TYPE, name, DEFAULT_SEPARATOR, defaultArray);
    }

    public long getLong(String name, long defaultValue) {
        return ConvertUtil.toLong(getString(name), defaultValue);
    }

    public long[] getLongArray(String name, long[] defaultArray) {
        return (long[])separateToArray(Long.TYPE, name, DEFAULT_SEPARATOR, defaultArray);
    }

    protected String normalizeName(String name) {
        return StringUtil.fixedCharToCamel(name, "-_");
    }

    public String getString(String name, String defaultValue) {
        name = normalizeName(name);
        return getStringImpl(name, defaultValue);
    }

    public String[] getStringArray(String name, String[] defaultArray) {
        name = normalizeName(name);
        return getStringArrayImpl(name, defaultArray);
    }

    protected abstract String getStringImpl(String name, String defaultValue);

    protected abstract String[] getStringArrayImpl(String name, String[] defaultArray);

    public UploadFile getUploadFile(String name) {
        return null;
    }

    public Set<String> getUploadFileNames() {
        return new HashSet<String>(0);
    }

    @Override
    public void apply(Object target) {
        if (target == null) {
            return;
        }
        MagicObject magicObject = MagicObject.wrap(target);
        Map<String, Property> properties = magicObject.getMagicClass().getProperties();

        for (Map.Entry<String, Property> entry: properties.entrySet()) {
            if (!entry.getValue().isWritable()) {
                 continue;
            }
            String name = entry.getKey();
            Property property = entry.getValue();

            if (property.getPropertyClass().isArray()) {
                Object valueArray = separateToArray(property.getPropertyClass().getTargetClass().getComponentType(), name, DEFAULT_SEPARATOR, null);
                property.setValueExt(target, valueArray);
            } else if (property.getPropertyClass().isCollectionLike()) {
                // TODO 由于泛型擦除，功能待实现
            } else {
                String value = getString(name);
                property.setValueExt(target, value);
            }

        }
    }

    protected Object separateToArray(Class<?> componentType, String name, String separator, Object defaultValue) {
        String[] values = getStringArray(name);
        if (values == null) {
            return defaultValue;
        }
        int len = values.length;
        if (len > 1) {
            return convertStringsToArray(componentType, len, Arrays.asList(values));
        }

        String rawValue = values[0];
        if (rawValue == null) {
            return defaultValue;
        }
        List<String> newValues = StringUtil.splitTrim(rawValue, separator);
        return convertStringsToArray(componentType, newValues.size(), newValues);
    }

    private static Object convertStringsToArray(Class<?> componentType, int len, Iterable<String> values) {
        Object ret = Array.newInstance(componentType, len);
        int i = 0;
        for (String value : values) {
            Object v = ConvertUtil.convertTo(value, componentType, null);
            if (v != null) {
                Array.set(ret, i, v);
            }
            i++;
        }
        return ret;
    }

    private static final String DEFAULT_SEPARATOR = ",";

}
