package wint.mvc.view.types.json;

import java.io.Serializable;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultCode;

public class JsonResult implements Serializable {
	
	private static final long serialVersionUID = -4233795873654775539L;

	private boolean success;
	
	private int code;
	
	private String message;

	public JsonResult() {
		super();
	}
	
	public JsonResult(Result result) {
		this.success = result.isSuccess();
		ResultCode resultCode = result.getResultCode();
		if (resultCode != null) {
			this.code = resultCode.getCode();
			this.message = resultCode.getMessage();
		}
	}

	public JsonResult(boolean success, int code, String message) {
		super();
		this.success = success;
		this.code = code;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
