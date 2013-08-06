package wint.mvc.form.validator;

import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.config.FormConfig;

/**
 * @author pister
 * 2012-2-8 03:36:16
 */
public interface Validator {
	
	boolean validate(InnerFlowData flowData, FormConfig formConfig, String fieldName, String fieldValue);
	
	/**
	 * 
	 */
	void init();
	
}
