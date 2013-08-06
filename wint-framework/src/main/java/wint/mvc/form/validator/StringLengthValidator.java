package wint.mvc.form.validator;

import wint.lang.utils.StringUtil;

/**
 *
 * @author pister 2010-3-9
 */
public class StringLengthValidator extends AbstractValidator {

	private int min = Integer.MIN_VALUE;
	
	private int max = Integer.MAX_VALUE;
	
	public boolean validate(String fieldValue) {
		if (StringUtil.isEmpty(fieldValue)) {
			if (min <= 0) {
				return true;
			} else {
				return false;
			}
		} 
		int len = fieldValue.length();
		if (len < min) {
			return false;
		}
		if (len > max) {
			return false;
		}
		return true;
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
