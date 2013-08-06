package wint.mvc.form.validator;

import java.util.regex.Pattern;

import wint.lang.utils.StringUtil;

public class PhoneValidator extends AbstractValidator {

	private Pattern phonePattern;
	
	@Override
	public void init() {
		super.init();
		phonePattern = Pattern.compile("1\\d{10}");
	}
	
	@Override
	protected boolean validate(String fieldValue) {
		if (StringUtil.isEmpty(fieldValue)) {
			return true;
		} 
		return phonePattern.matcher(fieldValue).matches();
	}

}
