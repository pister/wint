package wint.mvc.form.config;

import java.util.Map;
import java.util.Set;

public class ParseResult {
	
	private Map<String, FormConfig> formConfigs;
	
	private Set<String> resourceNames;

	public ParseResult() {
		super();
	}

	public ParseResult(Map<String, FormConfig> formConfigs, Set<String> resourceNames) {
		super();
		this.formConfigs = formConfigs;
		this.resourceNames = resourceNames;
	}
	
	public Map<String, FormConfig> getFormConfigs() {
		return formConfigs;
	}

	public void setFormConfigs(Map<String, FormConfig> formConfigs) {
		this.formConfigs = formConfigs;
	}

	public Set<String> getResourceNames() {
		return resourceNames;
	}

	public void setResourceNames(Set<String> resourceNames) {
		this.resourceNames = resourceNames;
	}

}
