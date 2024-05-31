package wint.help.biz.result;

import java.io.Serializable;
import java.util.Map;

public class ResultCode extends MessageRender implements Serializable {
	
	private static final long serialVersionUID = -3124130374367913002L;
	
	private int code;
	
	private String name;
	
	public String toString() {
		return getMessage();
	}

	public String getName() {
		if (name != null) {
			return name;
		}
		resolveNames();
		return name;
	}

	private void resolveNames() {
		Class<? extends ResultCode> clazz = getClass();
		synchronized (clazz) {
			Map<String, ResultCode> fields = ResultCodeTool.getEnumFields(clazz);
			for (Map.Entry<String, ResultCode> fieldEntry : fields.entrySet()) {
				String name = fieldEntry.getKey();
				ResultCode obj = fieldEntry.getValue();
				if (obj == this) {
					this.name = name;
				}
			}
		}
	}
	
	protected static ResultCode create() {
		StackTraceElement[] stes = new Exception().getStackTrace();
		return newResultCode(stes, -1);
	}
	
	protected static ResultCode create(int code) {
		StackTraceElement[] stes = new Exception().getStackTrace();
		return newResultCode(stes, code);
	}
	
	@SuppressWarnings("unchecked")
	private static ResultCode newResultCode(StackTraceElement[] stes, int code) {
		try {
			String callerClassName = getCallerClassName(stes);
			Class<ResultCode> callerClass = (Class<ResultCode>)Class.forName(callerClassName);
			if (!ResultCode.class.isAssignableFrom(callerClass)) {
				return null;
			}
			ResultCode resultCodeObj = (ResultCode)callerClass.newInstance();
			resultCodeObj.setCode(code);
			return resultCodeObj;
		} catch (Exception e) {
			return null;
		}
	}
	
	private static String getCallerClassName(StackTraceElement[] stes) {
		for (int i = 1; i < stes.length; ++i) {
			StackTraceElement ste = stes[i];
			if (ste.getMethodName().equals("<cinit>")) {
				continue;
			}
			return ste.getClassName();
		}
		return null;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return ResourceBundleUtil.getMessage(this);
	}
}
