package wint.mvc.form.validator;

import wint.lang.utils.StringUtil;

public class LongValidator extends AbstractValidator {

	private long min = Long.MIN_VALUE;
	
	private long max = Long.MAX_VALUE;
	
	@Override
	protected boolean validate(String fieldValue) {
		if (StringUtil.isEmpty(fieldValue)) {
			return true;
		}
		try {
			long value = Long.valueOf(fieldValue);
			if (value >= min && value <= max) {
				return true;
			}
		} catch(Exception e) {
			return false;
		}
		return false;
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

}
