package wint.mvc.flow;

import wint.mvc.module.Module;
import wint.mvc.parameters.Arguments;
import wint.mvc.parameters.Parameters;
import wint.mvc.template.Context;

/**
 * @author pister
 * 2012-4-7 下午08:56:08
 */
public interface InnerFlowData extends FlowData {
	
	/**
	 * 提交数据
	 */
	void commitData();
	
	/**
	 * 重设重定向标识
	 */
	void resetRedirected();
	
	/**
	 * 是否已经内部重定向
	 * @return
	 */
	boolean isForwardTo();
	
	/**
	 * 是否已经外部重定向
	 * @return
	 */
	boolean isSendRedirected();
	
	void setArguments(Arguments arguments);

    void setParameters(Parameters parameters);
	
	void setInnerContext(Context innerContext);
	
	void setContext(Context context);

	Module getModule();

	void setModule(Module module);

}
