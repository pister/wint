package wint.lang.convert.converts;

/**
 *
 * @author pister 2010-3-4
 */
public class BooleanConvert extends AbstractConvert<Boolean> {

	public Boolean convertTo(Object input, Boolean defaultValue) {
		if (input == null) {
			return defaultValue;
		}
		if (input instanceof Boolean) {
			return (Boolean)input;
		}
		return Boolean.valueOf(input.toString());
	}

	public Boolean getDefaultValue() {
		return Boolean.FALSE;
	}

}
