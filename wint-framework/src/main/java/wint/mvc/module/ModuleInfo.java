package wint.mvc.module;

import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicMethod;
import wint.lang.utils.StringUtil;
import wint.mvc.module.annotations.Action;
import wint.mvc.module.annotations.LimitAction;

public class ModuleInfo {

    private MagicClass targetClass;

    private MagicMethod targetMethod;

    private String defaultTarget;

    private boolean useDefaultMethod;

    // 这里没有用Set集合的原因是一般suffix只有1~2个，数组查询起来比较快。用Set内存开销大
    private String[] allowSuffix;

    public ModuleInfo(MagicClass targetClass, MagicMethod targetMethod, boolean useDefaultMethod) {
        super();
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.useDefaultMethod = useDefaultMethod;

        initLimit();
        initDefaultTarget();
    }

    private void initLimit() {
        LimitAction limitAction = targetMethod.getTargetMethod().getAnnotation(LimitAction.class);
        if (limitAction == null) {

        }
        String[] allowSuffix = limitAction.allowSuffix();
        if (allowSuffix == null || allowSuffix.length == 0) {
            this.allowSuffix = allowSuffix;
            return;
        }
        String[] targetAllowSuffix = new String[allowSuffix.length];
        int i = 0;
        for (String as : allowSuffix) {
            targetAllowSuffix[i] = normalizeSuffix(as);
            i++;
        }
        this.allowSuffix = targetAllowSuffix;
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

    private static String normalizeSuffix(String suffix) {
        if (StringUtil.isEmpty(suffix)) {
            return suffix;
        }
        if (suffix.startsWith(".")) {
            suffix = suffix.substring(1);
        }
        return suffix.toLowerCase();
    }


    public boolean acceptSuffix(String suffix) {
        if (allowSuffix == null || allowSuffix.length == 0) {
            return true;
        }
        if (suffix == null) {
            return false;
        }
        suffix = normalizeSuffix(suffix);
        for (String s : allowSuffix) {
            if (StringUtil.equals(s, suffix)) {
                return true;
            }
        }
        return false;
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
