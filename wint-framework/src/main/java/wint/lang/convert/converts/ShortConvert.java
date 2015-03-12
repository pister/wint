package wint.lang.convert.converts;

/**
 *
 * @author pister 2010-3-4
 */
public class ShortConvert extends NumberConvert<Short> {
	
	private static final short defaultValue = (short)0;
	
	public Short convertTo(Object input, Short defaultValue) {
		Number num = (Number)super.convertToNumber(input, defaultValue);
        if (num == null) {
            return defaultValue;
        }
		return Short.valueOf(num.shortValue());
	}

	public Short getDefaultValue() {
		return defaultValue;
	}

}
