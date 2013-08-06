package wint.mvc.servlet.mock;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

public class FilterConfigMock implements FilterConfig {

	private String filterName;
	
	private ServletContext servletContext;
	
	private Hashtable<String, String> initParameter = new Hashtable<String, String>();
	
	public FilterConfigMock(String filterName, ServletContext servletContext) {
		super();
		this.filterName = filterName;
		this.servletContext = servletContext;
	}

	public String getFilterName() {
		return filterName;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String getInitParameter(String name) {
		return initParameter.get(name);
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getInitParameterNames() {
		return initParameter.keys();
	}

}
