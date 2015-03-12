package wint.lang.convert.converts;

/**
 *
 * @author pister 2010-3-4
 */
public class DoubleConvert extends NumberConvert<Double>  {
	
	public Double convertTo(Object input, Double defaultValue) {
		Number num = (Number)super.convertToNumber(input, defaultValue);
        if (num == null) {
            return defaultValue;
        }
		return Double.valueOf(num.doubleValue());
	}

	public Double getDefaultValue() {
		return 0.0;
	}

}
