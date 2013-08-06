package wint.mvc.form.validator;

import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.config.FormConfig;

/**
 * @author pister
 *
 */
public abstract class AbstractValidator implements Validator {

	public void init() {

	}

	public final boolean validate(InnerFlowData flowData, FormConfig formConfig, String fieldName, String fieldValue) {
		return validate(fieldValue);
	}
	
	protected abstract boolean validate(String fieldValue);

}
