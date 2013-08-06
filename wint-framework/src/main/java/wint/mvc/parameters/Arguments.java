package wint.mvc.parameters;

public interface Arguments {
	
	int length();
	
	String getString(int index, String defaultValue);

	String getString(int index);
	
	int getInt(int index, int defaultValue);
	
	int getInt(int index);
	
	long getLong(int index, long defaultValue);
	
	long getLong(int index);
	
	float getFloat(int index, float defaultValue);
	
	float getFloat(int index);
	
	double getDouble(int index, double defaultValue);
	
	double getDouble(int index);

}
