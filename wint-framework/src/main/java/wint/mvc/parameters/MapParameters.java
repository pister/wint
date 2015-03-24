package wint.mvc.parameters;

import wint.lang.magic.Transformer;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;

import java.util.*;

public class MapParameters extends AbstractParameters {

	private Map<String, String[]> mapParameters;

	public MapParameters(Map<String, String[]> mapParameters) {
		super();
        this.mapParameters = MapUtil.newHashMap();
        addParameters(mapParameters);
	}

    public MapParameters(Parameters baseParameters) {
        super();
        this.mapParameters = MapUtil.newHashMap(baseParameters.getParameterMap());
    }

    public void addParameters(Parameters parameters) {
        addParameters(parameters.getParameterMap());
    }

    public void addParameters(Map<String, String[]> mapParameters) {
        for (Map.Entry<String, String[]> entry : mapParameters.entrySet()) {
            String paramName = normalizeName(entry.getKey());
            String[] values = mapParameters.get(entry.getKey());
            this.mapParameters.put(paramName, values);
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(mapParameters);
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

    @Override
    public String toString() {
        return "MapParameters{" +
                "mapParameters=" + MapUtil.join(mapParameters, "=", ",", new Transformer<String[], String>() {
            @Override
            public String transform(String[] object) {
                return Arrays.toString(object);
            }
        }) + '}';
    }
}
