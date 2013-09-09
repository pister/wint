package wint.mvc.module;

import wint.lang.magic.MagicList;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.Context;

/**
 * @author pister 2012-3-4 06:19:04
 */
public class NopModule implements ExecutionModule {
	
	private String target;

	private String moduleType;

	public NopModule(String target, String moduleType) {
		super();
		this.target = target;
		this.moduleType = moduleType;
	}

	public String execute(InnerFlowData flowData, Context context, MagicList<Object> indexedParameters) {
		return target;
	}

	public String getType() {
		return moduleType;
	}

	public String getModuleType() {
		return moduleType;
	}

	public boolean isDoAction() {
		return false;
	}

	public String getDefaultTarget() {
		return null;
	}

	public ModuleInfo getModuleInfo() {
		return null;
	}

}
