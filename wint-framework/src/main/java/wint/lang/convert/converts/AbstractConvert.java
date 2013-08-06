package wint.lang.convert.converts;

public abstract class AbstractConvert<T> implements Convert<T> {

	public T convertTo(Object input) {
		return convertTo(input, getDefaultValue());
	}

}
