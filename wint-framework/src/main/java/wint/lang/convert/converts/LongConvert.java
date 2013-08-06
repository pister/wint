package wint.lang.convert.converts;

/**
 *
 * @author pister 2010-3-4
 */
public class LongConvert extends NumberConvert<Long> {

	public Long convertTo(Object input, Long defaultValue) {
		Number num = (Number)super.convertToNumber(input, defaultValue);
		return Long.valueOf(num.longValue());
	}

	public Long getDefaultValue() {
		return 0L;
	}

}
