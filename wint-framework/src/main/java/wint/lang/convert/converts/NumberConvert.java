package wint.lang.convert.converts;


/**
 *
 * @author pister 2010-3-4
 */
public abstract class NumberConvert<T> extends AbstractConvert<T> {

	protected Object convertToNumber(Object input, Object defaultValue) {
		if (input instanceof Number) {
			Number num = (Number)input;
			return num;
		}
		try {
			return Double.valueOf(String.valueOf(input));
		} catch(Exception e) {
			if (defaultValue == null) {
				return Integer.valueOf(0);
			} else {
				return defaultValue;
			}
		}
	}
	
}
