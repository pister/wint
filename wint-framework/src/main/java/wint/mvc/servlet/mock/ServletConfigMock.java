package wint.mvc.servlet.mock;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import wint.lang.magic.MagicMap;
import wint.lang.utils.MapUtil;

public class ServletConfigMock implements ServletConfig {

	private MagicMap initParameters = MagicMap.newMagicMap();
	
	private ServletContext servletContext;
	
	public ServletConfigMock(MagicMap initConfigParameters, MagicMap servletContextInitParameters) {
		super();
		this.initParameters = initConfigParameters;
		servletContext = new ServletContextMock(toStringMap(servletContextInitParameters));
	}
	
	private static Map<String, String> toStringMap(Map<String, Object> m) {
		Map<String, String> ret = MapUtil.newHashMap();
		for (Map.Entry<String, Object> entry : m.entrySet()) {
			Object v = entry.getValue();
			if (v == null) {
				continue;
			}
			ret.put(entry.getKey(), v.toString());
		}
		return ret;
	}
	
	public ServletConfigMock() {
		super();
	}

	public String getInitParameter(String name) {
		return initParameters.getString(name);
	}

	public Enumeration<?> getInitParameterNames() {
		return Collections.enumeration(initParameters.keySet());
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void addParameter(String name, String value) {
		initParameters.put(name, value);
	}

	public String getServletName() {
		return "MockServlet";
	}

}
