package wint.mvc.form.validator;

import java.util.regex.Pattern;

import wint.lang.utils.StringUtil;


/**
 * @author pister
 *
 */
public class EmailValidator extends AbstractValidator {

	private Pattern emailPattern;
	
	@Override
	public void init() {
		super.init();
		emailPattern = Pattern.compile("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}

	@Override
	protected boolean validate(String fieldValue) {
		if (StringUtil.isEmpty(fieldValue)) {
			return true;
		} 
		return emailPattern.matcher(fieldValue).matches();
	}

}
