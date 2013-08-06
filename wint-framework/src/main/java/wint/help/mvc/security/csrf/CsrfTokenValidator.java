package wint.help.mvc.security.csrf;

import wint.lang.utils.StringUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.config.FormConfig;
import wint.mvc.form.validator.Validator;

/**
 * @author pister 2012-3-11 下午02:24:56
 */
public class CsrfTokenValidator implements Validator {

	public void init() {
		
	}

	public boolean validate(InnerFlowData flowData, FormConfig formConfig, String fieldName, String fieldValue) {
		if (StringUtil.isEmpty(fieldValue)) {
			return false;
		}
		String tokenFromSession = CsrfTokenUtil.token();
		CsrfTokenUtil.nextToken();
		if (!StringUtil.equals(tokenFromSession, fieldValue)) {
			return false;
		}
		return true;
	}

}
