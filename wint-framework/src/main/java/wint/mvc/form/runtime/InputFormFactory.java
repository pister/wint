package wint.mvc.form.runtime;

import java.util.Collections;
import java.util.Map;

import wint.lang.utils.MapUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.Form;

/**
 * @author pister
 * 2012-2-12 01:58:55
 */
public class InputFormFactory implements FormFactory {

	private InnerFlowData flowData;
	
	private Map<String, RunTimeForm> resultForms = MapUtil.newHashMap();
	
	public InputFormFactory(InnerFlowData flowData) {
		super();
		this.flowData = flowData;
	}

	public RunTimeForm getForm(String name) {
		RunTimeForm resultForm = resultForms.get(name);
		if (resultForm != null) {
			return resultForm;
		}
		Form form = flowData.getForm(name);
		return new DefaultRunTimeForm(form);
	}	

    public Map<String, RunTimeForm> getForms() {
        return Collections.unmodifiableMap(resultForms);
    }

	public void addResultForm(String name, RunTimeForm runTimeForm) {
		resultForms.put(name, runTimeForm);
	}

    public RunTimeForm getResultForm(String name) {
        return resultForms.get(name);
    }

	public void removeResultForm(String name) {
		resultForms.remove(name);
	}

}
