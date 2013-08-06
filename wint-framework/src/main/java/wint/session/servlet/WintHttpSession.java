package wint.session.servlet;

import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import wint.session.config.SessionConfig;
import wint.session.store.DefaultSessionStore;
import wint.session.store.SessionStore;

@SuppressWarnings("deprecation")
public class WintHttpSession implements HttpSession {

	private ServletContext servletContext;
	
	private WintSessionHttpServletRequest wintSessionHttpServletRequest;

	private WintSessionHttpServletResponse wintSessionHttpServletResponse;
	
	private SessionStore sessionStore;
	
	public WintHttpSession(WintSessionHttpServletRequest wintSessionHttpServletRequest, WintSessionHttpServletResponse wintSessionHttpServletResponse) {
		super();
		this.wintSessionHttpServletRequest = wintSessionHttpServletRequest;
		this.wintSessionHttpServletResponse = wintSessionHttpServletResponse;
	}

	public void init(ServletContext servletContext, SessionConfig sessionConfig) {
		this.servletContext = servletContext;
		sessionStore = new DefaultSessionStore(wintSessionHttpServletRequest, wintSessionHttpServletResponse);
		sessionStore.init(sessionConfig);
	}
	
	public void commit() {
		sessionStore.commit();
	}
	
	public long getCreationTime() {
		return sessionStore.getCreateTime();
	}

	public String getId() {
		return sessionStore.getSessionId();
	}

	public long getLastAccessedTime() {
		return sessionStore.getLastAccessedTime();
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setMaxInactiveInterval(int interval) {
		sessionStore.setMaxInactiveInterval(interval);
	}

	public int getMaxInactiveInterval() {
		return sessionStore.getMaxInactiveInterval();
	}

	public HttpSessionContext getSessionContext() {
		throw new UnsupportedOperationException();
	}

	public Object getAttribute(String name) {
		return sessionStore.getAttribute(name);
	}

	public Object getValue(String name) {
		return getAttribute(name);
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getAttributeNames() {
		return Collections.enumeration(sessionStore.getAttributeNames());
	}

	@SuppressWarnings("unchecked")
	public String[] getValueNames() {
		return (String[])Collections.list(getAttributeNames()).toArray(new String[0]);
	}

	public void setAttribute(String name, Object value) {
		sessionStore.setAttribute(name, value);
	}

	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}

	public void removeAttribute(String name) {
		sessionStore.removeAttribute(name);
	}

	public void removeValue(String name) {
		removeAttribute(name);
	}

	public void invalidate() {
		sessionStore.invalidate();
	}

	public boolean isNew() {
		return sessionStore.isNew();
	}

}
