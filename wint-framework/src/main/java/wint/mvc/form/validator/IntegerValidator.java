package wint.mvc.form.validator;

import wint.lang.utils.StringUtil;

/**
 * @author pister
 * 2010-3-3 10:20:12
 * @version 1.0
 */
public class IntegerValidator extends AbstractValidator {

	private int min = Integer.MIN_VALUE;
	
	private int max = Integer.MAX_VALUE;
	
	public boolean validate(String fieldValue) {
		if (StringUtil.isEmpty(fieldValue)) {
			return true;
		}
		try {
			int value = Integer.valueOf(fieldValue);
			if (value >= min && value <= max) {
				return true;
			}
		} catch(Exception e) {
			return false;
		}
		return false;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

}
