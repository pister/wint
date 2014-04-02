package wint.mvc.form;

import java.util.Map;

import wint.lang.magic.MagicMap;
import wint.mvc.form.validator.ValidateResult;
import wint.mvc.parameters.Parameters;

/**
 * @author pister
 * 2012-2-12 01:29:14
 */
public interface Form {
	
	boolean validate();
	
	ValidateResult getValidateResult();
	
	String getName();
	
	boolean apply(Object target);
	
	void hold(Object object);
	
	Map<String, Field> getFields();

    Parameters getValues();
	
	void holdRequest();
	
	void clearHold();
	
}
