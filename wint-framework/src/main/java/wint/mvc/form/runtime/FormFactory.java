package wint.mvc.form.runtime;

import java.util.Map;

public interface FormFactory {
	
	RunTimeForm getForm(String name);
	
	void addResultForm(String name, RunTimeForm runTimeForm);
	
	void removeResultForm(String name);

    RunTimeForm getResultForm(String name);

    Map<String, RunTimeForm> getForms();
}
