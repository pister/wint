package wint.mvc.parameters;

import java.util.Date;
import java.util.Set;

/**
 * 
 * 提供这个类的原因是velocity模板不支持方法重载
 * @author pister 2012-3-10 下午08:54:25
 */
public class TemplateParameters {

	private Parameters parameters;

	public TemplateParameters(Parameters parameters) {
		super();
		this.parameters = parameters;
	}

	public boolean getBoolean(String name) {
		return parameters.getBoolean(name);
	}

	public Date getDate(String name, String format) {
		return parameters.getDate(name, format);
	}

	public double getDouble(String name) {
		return parameters.getDouble(name);
	}

	public double[] getDoubleArray(String name) {
		return parameters.getDoubleArray(name);
	}

	public float getFloat(String name) {
		return parameters.getFloat(name);
	}

	public float[] getFloatArray(String name) {
		return parameters.getFloatArray(name);
	}

	public int getInt(String name) {
		return parameters.getInt(name);
	}

	public int[] getIntArray(String name) {
		return parameters.getIntArray(name);
	}

	public long getLong(String name) {
		return parameters.getLong(name);
	}

	public long[] getLongArray(String name) {
		return parameters.getLongArray(name);
	}

	public Set<String> getNames() {
		return parameters.getNames();
	}

	public String getString(String name) {
		return parameters.getString(name);
	}

	public String[] getStringArray(String name) {
		return parameters.getStringArray(name);
	}

}
