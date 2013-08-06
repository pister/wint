package wint.mvc.parameters;

import java.util.List;

import wint.lang.convert.ConvertUtil;

public class ListArguments implements Arguments {
	
	private List<String> arguments;

	public ListArguments(List<String> arguments) {
		super();
		this.arguments = arguments;
	}

	public double getDouble(int index, double defaultValue) {
		return ConvertUtil.toDouble(getString(index), defaultValue);
	}

	public double getDouble(int index) {
		return getDouble(index, 0.0);
	}

	public float getFloat(int index, float defaultValue) {
		return ConvertUtil.toFloat(getString(index), defaultValue);
	}

	public float getFloat(int index) {
		return getFloat(index, 0.0f);
	}

	public int getInt(int index, int defaultValue) {
		return ConvertUtil.toInt(getString(index), defaultValue);
	}

	public int getInt(int index) {
		return getInt(index, 0);
	}

	public long getLong(int index, long defaultValue) {
		return ConvertUtil.toLong(getString(index), defaultValue);
	}

	public long getLong(int index) {
		return getLong(index, 0L);
	}

	public String getString(int index, String defaultValue) {
		if (index >= arguments.size()) {
			return defaultValue;
		}
		String value = arguments.get(index);
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

	public String getString(int index) {
		return getString(index, null);
	}

	public int length() {
		return arguments.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		sb.append("[");
		for (int i =0, len = arguments.size(); i < len; ++i) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(i);
			sb.append(": ");
			sb.append(arguments.get(i));
		}
		sb.append("]");
		return sb.toString();
	}

}
