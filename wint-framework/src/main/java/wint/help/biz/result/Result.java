package wint.help.biz.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public interface Result extends Serializable {
	
	boolean isSuccess();
	
	void setSuccess(boolean success);
	
	ResultCode getResultCode();
	
	void setResultCode(ResultCode resultCode);

    /**
     * 同setFieldResultCode
     * @param fieldName
     * @param resultCode
     */
	void setResultCode(String fieldName, ResultCode resultCode);

    /**
     * 给某个字段设置resultcode,这样会在form中绑定，利用form统一在页面上显示错误信息
     * @param fieldName
     * @param resultCode
     * @deprecated see  setResultCode
     */
    void setFieldResultCode(String fieldName, ResultCode resultCode);

    boolean isHasFieldResultCodes();

	Map<String, Object> getModels();
	
}
