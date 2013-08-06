package wint.lang.convert.converts;

/**
 *
 * @author pister 2010-3-4
 */
public class FloatConvert extends NumberConvert<Float> {
	
	public Float convertTo(Object input, Float defaultValue) {
		Number num = (Number)super.convertToNumber(input, defaultValue);
		return Float.valueOf(num.floatValue());
	}

	public Float getDefaultValue() {
		return 0.0f;
	}
	
}
