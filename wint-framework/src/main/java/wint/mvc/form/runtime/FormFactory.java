package wint.mvc.form.runtime;

public interface FormFactory {
	
	RunTimeForm getForm(String name);
	
	void addResultForm(String name, RunTimeForm runTimeForm);
	
	void removeResultForm(String name);

    RunTimeForm getResultForm(String name);
}
