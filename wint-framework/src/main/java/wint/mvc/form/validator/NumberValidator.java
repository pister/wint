package wint.mvc.form.validator;

import wint.lang.utils.StringUtil;

public class NumberValidator extends AbstractValidator {

	private Double min;
	
	private Double max;
	
	public boolean validate(String fieldValue) {
		if (StringUtil.isEmpty(fieldValue)) {
			return true;
		}
		try {
			double value = Double.parseDouble(fieldValue);
            if (min != null && value < min) {
                return false;
            }
            if (max != null && value > max) {
                return false;
            }
            return true;
		} catch(Exception e) {
			return false;
		}
	}

	public double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}
	
}
