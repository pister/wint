package wint.mvc.parameters;

import java.util.Map;
import java.util.Set;

public class MapParameters extends AbstractParameters {

	private Map<String, String[]> mapParameters;
	
	public MapParameters(Map<String, String[]> mapParameters) {
		super();
		this.mapParameters = mapParameters;
	}

	public Set<String> getNames() {
		return mapParameters.keySet();
	}

	public String getString(String name, String defaultValue) {
		String[] ret = mapParameters.get(name);
		if (ret == null) {
			return defaultValue;
		}
		if (ret.length == 0) {
			return defaultValue;
		}
		return ret[0];
	}

	public String[] getStringArray(String name, String[] defaultArray) {
		String[] ret =  mapParameters.get(name);
		if (ret == null) {
			return defaultArray;
		} else {
			return ret;
		}
	}

}
