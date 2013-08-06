package wint.help.biz.result;

public class StringResultCode extends ResultCode {

	private static final long serialVersionUID = -7159442561135670267L;
	
	private String message;

	@Override
	public int getCode() {
		return -1;
	}

	public StringResultCode(String message) {
		super();
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
}
