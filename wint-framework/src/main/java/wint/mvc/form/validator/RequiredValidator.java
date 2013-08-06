package wint.mvc.form.validator;

import wint.lang.utils.StringUtil;

/**
 * @author pister
 * 2010-3-3 10:16:57
 * @version 1.0
 */
public class RequiredValidator extends AbstractValidator {

	public boolean validate(String fieldValue) {
		if (StringUtil.isEmpty(fieldValue)) {
			return false;
		}
		return true;
	}

}
