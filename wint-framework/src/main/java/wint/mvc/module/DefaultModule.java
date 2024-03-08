package wint.mvc.module;

import wint.lang.collections.FastStack;
import wint.lang.exceptions.MethodInvokeException;
import wint.lang.exceptions.MethodNotAllowedException;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicList;
import wint.lang.magic.MagicObject;
import wint.lang.misc.profiler.Profiler;
import wint.lang.utils.ArrayUtil;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.flow.StatusCodes;
import wint.mvc.restful.method.FlowDataCreator;
import wint.mvc.template.Context;
import wint.mvc.view.types.ViewTypes;


/**
 * @author pister
 * 2011-12-30 04:54:32
 */
public class DefaultModule implements ExecutionModule {

    private MagicObject targetObject;

    private ModuleInfo moduleInfo;

    private MagicList<MagicClass> parameterTypes;

    private String moduleType;

    private FlowDataCreator restfulFlowDataCreator;

    public DefaultModule(MagicObject targetObject, ModuleInfo moduleInfo, String moduleType) {
        super();
        this.targetObject = targetObject;
        this.moduleInfo = moduleInfo;
        this.parameterTypes = moduleInfo.getTargetMethod().getParameterTypes();
        this.moduleType = moduleType;
    }

    public String execute(InnerFlowData flowData, Context context, MagicList<Object> indexedParameters) {
        try {
            Profiler.enter("execute module: " + flowData.getTarget());
            String suffix = flowData.getSuffix();
            if (!moduleInfo.acceptSuffix(suffix)) {
                flowData.setStatusCode(StatusCodes.SC_FORBIDDEN, "Can not execute by suffix: " + suffix);
                return null;
            }
            if (moduleInfo.isDoAction()) {
                flowData.setTarget(null);
            }
            FlowData executeFlowData;
            if (restfulFlowDataCreator != null) {
                executeFlowData = restfulFlowDataCreator.create(flowData);
                executeFlowData.setViewType(ViewTypes.JSON_VIEW_TYPE);
            } else {
                executeFlowData = flowData;
            }
            String ret = executeModule(executeFlowData, context, indexedParameters);
            if (!StringUtil.isEmpty(ret)) {
                return ret;
            }
            ret = flowData.getTarget();
            if (!StringUtil.isEmpty(ret)) {
                return ret;
            }
            return moduleInfo.getDefaultTarget();
        } catch (MethodNotAllowedException e) {
            flowData.setStatusCode(StatusCodes.SC_METHOD_NOT_ALLOWED, e.getMessage());
            return null;
        } catch (MethodInvokeException e) {
            Throwable targetException = e.getCause();
            if(targetException instanceof MethodNotAllowedException) {
                flowData.setStatusCode(StatusCodes.SC_METHOD_NOT_ALLOWED, e.getMessage());
                return null;
            }
            throw e;
        } finally {
            Profiler.release();
        }
    }


    protected String executeModule(FlowData flowData, Context context, MagicList<Object> indexedParameters) {
        if (CollectionUtil.isEmpty(parameterTypes)) {
            return invoke(ArrayUtil.EMPTY_OBJECT_ARRAY);
        }
        final int argumentLength = parameterTypes.size();
        Object[] arguments = new Object[argumentLength];
        FastStack<Object> indexedArgumentsStack = new FastStack<Object>(indexedParameters);
        for (int i = 0; i < argumentLength; ++i) {
            MagicClass targetMagicClass = parameterTypes.get(i);
            if (targetMagicClass.isInstanceOf(flowData)) {
                arguments[i] = flowData;
                continue;
            }
            if (targetMagicClass.isInstanceOf(context)) {
                arguments[i] = context;
                continue;
            }
            if (indexedArgumentsStack.isEmpty()) {
                continue;
            }
            Object argument = indexedArgumentsStack.pop();
            arguments[i] = argument;
        }

        return invoke(arguments);
    }

    private String invoke(Object[] arguments) {
        Object ret = moduleInfo.getTargetMethod().invoke(targetObject, arguments);
        if (ret == null) {
            return null;
        } else {
            return ret.toString();
        }
    }

    public String getType() {
        return moduleType;
    }

    public boolean isDoAction() {
        return moduleInfo.isDoAction();
    }

    public String getDefaultTarget() {
        return moduleInfo.getDefaultTarget();
    }

    public ModuleInfo getModuleInfo() {
        return moduleInfo;
    }

    public boolean isRestful() {
        return restfulFlowDataCreator != null;
    }

    public FlowDataCreator getRestfulFlowDataCreator() {
        return restfulFlowDataCreator;
    }

    public void setRestfulFlowDataCreator(FlowDataCreator restfulFlowDataCreator) {
        this.restfulFlowDataCreator = restfulFlowDataCreator;
    }
}
