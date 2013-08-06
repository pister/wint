package wint.mvc.parameters;

import java.util.Date;
import java.util.Set;

public interface Parameters {
	
	Set<String> getNames();
	
	String getString(String name, String defaultValue);
	
	String getString(String name);
	
	int getInt(String name, int defaultValue);
	
	int getInt(String name);
	
	boolean getBoolean(String name, boolean defaultValue);
	
	boolean getBoolean(String name);
	
	long getLong(String name, long defaultValue);
	
	long getLong(String name);
	
	float getFloat(String name, float defaultValue);
	
	float getFloat(String name);
	
	double getDouble(String name, double defaultValue);
	
	double getDouble(String name);
	
	int[] getIntArray(String name, int[] defaultArray);
	
	int[] getIntArray(String name);
	
	String[] getStringArray(String name, String[] defaultArray);
	
	String[] getStringArray(String name);
	
	long[] getLongArray(String name, long[] defaultArray);
	
	long[] getLongArray(String name);
	
	float[] getFloatArray(String name, float[] defaultArray);
	
	float[] getFloatArray(String name);
	
	double[] getDoubleArray(String name, double[] defaultArray);
	
	double[] getDoubleArray(String name);
	
	Date getDate(String name, Date defaultDate);
	
	Date getDate(String name);
	
	Date getDate(String name, String format, Date defaultDate);
	
	Date getDate(String name, String format);

}
