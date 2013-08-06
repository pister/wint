package wint.mvc.servlet.mock;

import java.util.Collections;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import wint.lang.magic.MagicMap;

@SuppressWarnings("deprecation")
public class HttpSessionMock implements HttpSession {

	private MagicMap attributes = MagicMap.newMagicMap();
	
	private long startTime = System.currentTimeMillis();

	private String id = UUID.randomUUID().toString();
	
	private ServletContext servletContext;
	
	private int maxInactiveInterval = 10 * 60 * 1000;
	
	public HttpSessionMock(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public Enumeration<?> getAttributeNames() {
		return Collections.enumeration(attributes.keySet());
	}

	public long getCreationTime() {
		return startTime;
	}

	public String getId() {
		return id;
	}

	public long getLastAccessedTime() {
		return startTime;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public HttpSessionContext getSessionContext() {
		return null;
	}

	public Object getValue(String name) {
		return attributes.get(name);
	}

	public String[] getValueNames() {
		return attributes.keySet().toArray(new String[0]);
	}

	public void invalidate() {
		attributes.clear();
	}

	public boolean isNew() {
		return false;
	}

	public void putValue(String name, Object value) {
		attributes.put(name, value);
	}

	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	public void removeValue(String name) {
		attributes.remove(name);
	}

	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}

}
