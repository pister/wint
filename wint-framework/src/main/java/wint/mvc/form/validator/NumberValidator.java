package wint.mvc.form.validator;

import wint.lang.utils.StringUtil;

public class NumberValidator extends AbstractValidator {

	private double min = Double.MIN_VALUE;
	
	private double max = Double.MAX_VALUE;
	
	public boolean validate(String fieldValue) {
		if (StringUtil.isEmpty(fieldValue)) {
			return true;
		}
		try {
			double value = Double.parseDouble(fieldValue);
			if (value >= min && value <= max) {
				return true;
			}
		} catch(Exception e) {
			return false;
		}
		return false;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}
	
}
