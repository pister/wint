package wint.mvc.parameters;

import java.util.Date;

import wint.lang.convert.ConvertUtil;
import wint.lang.utils.StringUtil;

public abstract class AbstractParameters implements Parameters {

	public boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	public Date getDate(String name) {
		return getDate(name, (Date)null);
	}

	public Date getDate(String name, String format) {
		return getDate(name, format, (Date)null);
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
		String[] stringArray = getStringArray(name);
		if (stringArray == null) {
			return defaultArray;
		}
		double[] ret = new double[stringArray.length];
		for (int i = 0, len = stringArray.length; i < len; ++i) {
			ret[i] = ConvertUtil.toDouble(stringArray[i], 0.0);
		}
		return ret;
	}

	public float getFloat(String name, float defaultValue) {
		return ConvertUtil.toFloat(getString(name), defaultValue);
	}

	public float[] getFloatArray(String name, float[] defaultArray) {
		String[] stringArray = getStringArray(name);
		if (stringArray == null) {
			return defaultArray;
		}
		float[] ret = new float[stringArray.length];
		for (int i = 0, len = stringArray.length; i < len; ++i) {
			ret[i] = ConvertUtil.toFloat(stringArray[i], 0.0f);
		}
		return ret;
	}

	public int getInt(String name, int defaultValue) {
		return ConvertUtil.toInt(getString(name), defaultValue);
	}

	public int[] getIntArray(String name, int[] defaultArray) {
		String[] stringArray = getStringArray(name);
		if (stringArray == null) {
			return defaultArray;
		}
		int[] ret = new int[stringArray.length];
		for (int i = 0, len = stringArray.length; i < len; ++i) {
			ret[i] = ConvertUtil.toInt(stringArray[i], 0);
		}
		return ret;
	}

	public long getLong(String name, long defaultValue) {
		return ConvertUtil.toLong(getString(name), defaultValue);
	}

	public long[] getLongArray(String name, long[] defaultArray) {
		String[] stringArray = getStringArray(name);
		if (stringArray == null) {
			return defaultArray;
		}
		long[] ret = new long[stringArray.length];
		for (int i = 0, len = stringArray.length; i < len; ++i) {
			ret[i] = ConvertUtil.toLong(stringArray[i], 0L);
		}
		return ret;
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


}
