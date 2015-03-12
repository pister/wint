package wint.lang.convert.converts;

/**
 *
 * @author pister 2010-3-4
 */
public class IntConvert extends NumberConvert<Integer> {

	public Integer convertTo(Object input, Integer defaultValue) {
		Number num = (Number)super.convertToNumber(input, defaultValue);
        if (num == null) {
            return defaultValue;
        }
		return Integer.valueOf(num.intValue());
	}

	public Integer getDefaultValue() {
		return 0;
	}

}
