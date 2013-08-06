package wint.mvc.form.validator;

import java.util.regex.Pattern;

import wint.lang.utils.StringUtil;

public class RegexValidator extends AbstractValidator {

	private String regex;
	
	private int flag = 0;
	
	private Pattern pattern;
	
	@Override
	public void init() {
		super.init();
		buildPattern();
	}

	public boolean validate(String fieldValue) {
		if (StringUtil.isEmpty(fieldValue)) {
			return true;
		} 
		return pattern.matcher(fieldValue).matches();
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
		buildPattern();
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	private void buildPattern() {
		if (regex == null) {
			return;
		}
		pattern = Pattern.compile(regex, flag);
	}
	
}
