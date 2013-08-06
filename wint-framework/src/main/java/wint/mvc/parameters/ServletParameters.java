package wint.mvc.parameters;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;

public class ServletParameters extends AbstractParameters {

	private Map<String, String[]> parameters = MapUtil.newHashMap();

	@SuppressWarnings("unchecked")
	public ServletParameters(HttpServletRequest httpServletRequest) {
		super();
		for (String name : (List<String>)Collections.list(httpServletRequest.getParameterNames())) {
			String paramName = normalizeName(name);
			String[] values = httpServletRequest.getParameterValues(name);
			parameters.put(paramName, values);
		}
	}

	public String getString(String name, String defaultValue) {
		String[] values = getStringArray(name, null);
		if (values == null || values.length == 0) {
			return defaultValue;
		}
		return values[0];
	}

	public String[] getStringArray(String name, String[] defaultArray) {
		name = normalizeName(name);
		String[] values = parameters.get(name);
		if (values == null || values.length == 0) {
			return defaultArray;
		}
		return values;
	}

	protected String normalizeName(String name) {
		return StringUtil.fixedCharToCamel(name, "-_");
	}
	
	public Set<String> getNames() {
		return parameters.keySet();
	}

}
