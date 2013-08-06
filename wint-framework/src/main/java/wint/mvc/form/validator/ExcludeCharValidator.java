package wint.mvc.form.validator;

import java.util.Set;

import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;

public class ExcludeCharValidator extends AbstractValidator {

	private Set<Character> execludedChars = CollectionUtil.newHashSet();
	
	@Override
	protected boolean validate(String value) {
		if (StringUtil.isEmpty(value)) {
			return true;
		}
		for (int pos = 0, len = value.length(); pos < len; ++pos) {
			char c = value.charAt(pos);
			if (execludedChars.contains(c)) {
				return false;
			}
		}
		return true;
	}

	public void setChars(String chars) {
		for (int pos = 0, len = chars.length(); pos < len; ++pos) {
			char c = chars.charAt(pos);
			execludedChars.add(c);
		}
	}
	
	
	
}
