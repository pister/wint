package wint.lang.convert.converts;


/**
 *
 * @author pister 2010-3-4
 */
public class StringConvert extends AbstractConvert<String> {

	public String convertTo(Object input, String defaultValue) {
		if (input == null) {
			return defaultValue;
		} else {
			return input.toString();
		}
	}

	public String getDefaultValue() {
		return null;
	}

}
