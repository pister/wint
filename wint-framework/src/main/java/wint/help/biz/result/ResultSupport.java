package wint.help.biz.result;

import wint.core.config.Constants;
import wint.lang.WintException;
import wint.mvc.flow.FlowData;
import wint.mvc.form.Field;
import wint.mvc.form.Form;
import wint.mvc.form.runtime.FormFactory;
import wint.mvc.form.runtime.InputFormFactory;
import wint.mvc.form.runtime.RunTimeForm;
import wint.mvc.holder.WintContext;

import java.util.HashMap;
import java.util.Map;


public class ResultSupport implements Result {

	private static final long serialVersionUID = -6867564865059422611L;

	private boolean success = false;
	
	private ResultCode resultCode;
	
	private Map<String, Object> models = new HashMap<String, Object>();

    private boolean hasFieldResultCodes = false;

	public ResultSupport() {
		this(false);
	}
	
	public ResultSupport(boolean success) {
		super();
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(ResultCode resultCode) {
		this.resultCode = resultCode;
	}

    public void setFieldResultCode(String fieldName, ResultCode resultCode) {
        setResultCode(fieldName, resultCode);
    }

    public void setResultCode(String fieldName, ResultCode resultCode) {
        FlowData flowData = WintContext.getFlowData();
        String lastFormName = (String)flowData.getAttribute(Constants.Form.LAST_FORM_NAME);
        if (lastFormName == null) {
            throw new WintException("you must call FormService.getForm() method before Result.setFieldResultCode on this thread.");
        }
        FormFactory formFactory = (FormFactory)flowData.getInnerContext().get(Constants.Form.TEMPLATE_FORM_FACTORY_NAME);

        RunTimeForm runTimeForm = formFactory.getForm(lastFormName);
        Field field = runTimeForm.get(fieldName);
        if (field == null) {
            throw new WintException("field not exist for name: " + fieldName);
        }
        field.setMessage(resultCode.getMessage());
        hasFieldResultCodes = true;
    }

    public Map<String, Object> getModels() {
		return models;
	}

    public boolean isHasFieldResultCodes() {
        return hasFieldResultCodes;
    }
}
