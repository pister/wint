package wint.help.biz.result;

import java.io.Serializable;
import java.util.Map;

public interface Result extends Serializable {
	
	boolean isSuccess();
	
	void setSuccess(boolean success);
	
	ResultCode getResultCode();
	
	void setResultCode(ResultCode resultCode);
	
	void setResultCode(ResultCode resultCode, ResultType resultType);
	
	Map<String, Object> getModels();
	
	ResultType getResultType();

	void setResultType(ResultType resultType);
	

}
