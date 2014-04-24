package wint.help.biz.result;

public class StringResultCode extends ResultCode {

	private static final long serialVersionUID = -7159442561135670267L;
	
	private String message;

	public StringResultCode(String message) {
		super();
        this.setCode(-1);
		this.message = message;
	}

    public StringResultCode(int code, String message) {
        super();
        this.setCode(code);
        this.message = message;
    }

	@Override
	public String getMessage() {
		return message;
	}
	
}
