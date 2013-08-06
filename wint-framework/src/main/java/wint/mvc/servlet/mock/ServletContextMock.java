package wint.mvc.servlet.mock;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ServletContextMock implements ServletContext {

	private Hashtable<String, Object> attributes = new Hashtable<String, Object>();
	
	private Hashtable<String, String> initParameter;
	
	public ServletContextMock(Map<String, String> initParameter) {
		super();
		this.initParameter = new Hashtable<String, String>(initParameter);
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public Enumeration<?> getAttributeNames() {
		return attributes.keys();
	}

	public ServletContext getContext(String uripath) {
		return null;
	}

	public String getInitParameter(String name) {
		return initParameter.get(name);
	}

	public Enumeration<?> getInitParameterNames() {
		return initParameter.keys();
	}

	public int getMajorVersion() {
		return 0;
	}

	public String getMimeType(String file) {
		return null;
	}

	public int getMinorVersion() {
		return 0;
	}

	public RequestDispatcher getNamedDispatcher(String name) {
		return null;
	}

	public String getRealPath(String path) {
		return null;
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		return null;
	}

	public URL getResource(String path) throws MalformedURLException {
		return null;
	}

	public InputStream getResourceAsStream(String path) {
		return null;
	}

	
	public Set<?> getResourcePaths(String path) {
		return null;
	}

	public String getServerInfo() {
		return null;
	}

	public Servlet getServlet(String name) throws ServletException {
		return null;
	}

	public String getServletContextName() {
		return null;
	}

	public Enumeration<?> getServletNames() {
		return null;
	}

	public Enumeration<?> getServlets() {
		return null;
	}

	public void log(String msg) {
		System.out.println(msg);
	}

	public void log(Exception exception, String msg) {
		System.out.println(msg);
		exception.printStackTrace();
	}

	public void log(String message, Throwable throwable) {
		System.out.println(message);
		throwable.printStackTrace();
	}

	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
	}

}
