package wint.mvc.parameters;

import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapParameters extends AbstractParameters {

	private Map<String, String[]> mapParameters;
	
	public MapParameters(Map<String, String[]> mapParameters) {
		super();
        Map<String, String[]> newMapParameters = MapUtil.newHashMap();
        for (Map.Entry<String, String[]> entry : mapParameters.entrySet()) {
            String paramName = normalizeName(entry.getKey());
            String[] values = mapParameters.get(entry.getKey());
            newMapParameters.put(paramName, values);
        }
        this.mapParameters = newMapParameters;
	}

	public Set<String> getNames() {
		return mapParameters.keySet();
	}

	public String getStringImpl(String name, String defaultValue) {
		String[] ret = mapParameters.get(name);
		if (ret == null) {
			return defaultValue;
		}
		if (ret.length == 0) {
			return defaultValue;
		}
		return StringUtil.trimToEmpty(ret[0]);
	}

	public String[] getStringArrayImpl(String name, String[] defaultArray) {
		String[] ret =  mapParameters.get(name);
		if (ret == null) {
			return defaultArray;
		} else {
			return ret;
		}
	}

}
