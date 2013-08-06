package wint.mvc.form;

import wint.core.service.Service;
import wint.mvc.flow.InnerFlowData;

public interface FormService extends Service {
	
	Form getForm(String name, InnerFlowData flowData);

}
