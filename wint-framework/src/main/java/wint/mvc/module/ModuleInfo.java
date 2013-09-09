package wint.mvc.module;

import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicMethod;
import wint.mvc.module.annotations.Action;

public class ModuleInfo {

	private MagicClass targetClass;
	
	private MagicMethod targetMethod;

	private String defaultTarget;

    private boolean useDefaultMethod;

	public ModuleInfo(MagicClass targetClass, MagicMethod targetMethod, boolean useDefaultMethod) {
		super();
		this.targetClass = targetClass;
		this.targetMethod = targetMethod;
        this.useDefaultMethod = useDefaultMethod;
		initDefaultTarget();
	}
	
	private void initDefaultTarget() {
		if (targetMethod == null) {
			return;
		}
		Action dt = targetMethod.getTargetMethod().getAnnotation(Action.class);
		if (dt != null) {
			defaultTarget = dt.defaultTarget();
		}
	}

	public boolean isDoAction() {
		if (targetMethod == null) {
			return false;
		}
		String methodName = targetMethod.getName();
		if (methodName.length() < 3) {
			return false;
		}
		if (!methodName.startsWith("do")) {
			return false;
		}
		char firstEventChar = methodName.charAt(2);
		if (Character.isLetter(firstEventChar) && Character.isLowerCase(firstEventChar)) {
			return false;
		}
		return true;
	}
	
	public MagicClass getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(MagicClass targetClass) {
		this.targetClass = targetClass;
	}

	public MagicMethod getTargetMethod() {
		return targetMethod;
	}
	
	public void setTargetMethod(MagicMethod targetMethod) {
		this.targetMethod = targetMethod;
	}

	public String getDefaultTarget() {
		return defaultTarget;
	}

    public boolean isUseDefaultMethod() {
        return useDefaultMethod;
    }

    public void setUseDefaultMethod(boolean useDefaultMethod) {
        this.useDefaultMethod = useDefaultMethod;
    }
}
