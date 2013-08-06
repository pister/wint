package wint.help.biz.result;

import java.util.HashMap;
import java.util.Map;


public class ResultSupport implements Result {

	private static final long serialVersionUID = -6867564865059422611L;

	private boolean success = false;
	
	private ResultCode resultCode;
	
	private ResultType resultType = ResultType.COMMON_TARGET;
	
	private Map<String, Object> models = new HashMap<String, Object>();
	
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

	public void setResultCode(ResultCode resultCode, ResultType resultType) {
		this.resultCode = resultCode;
		this.resultType = resultType;
	}

	public Map<String, Object> getModels() {
		return models;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

}
