package wint.lang.convert.converts;

public interface Convert<T> {
	
	T convertTo(Object input, T defaultValue);
	
	T convertTo(Object input);
	
	T getDefaultValue();

}
