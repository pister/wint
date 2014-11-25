package wint.sessionx.servlet;

import wint.sessionx.constants.AttrKeys;
import wint.sessionx.constants.SpecSessionKeys;
import wint.sessionx.cookie.WintCookie;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.store.SessionStore;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Collections;
import java.util.Enumeration;

@SuppressWarnings("deprecation")
public class WintHttpSession implements HttpSession {

	private ServletContext servletContext;

	private SessionStore sessionStore;

	public WintHttpSession(SessionStore sessionStore, ServletContext servletContext) {
		super();
        this.sessionStore = sessionStore;
        this.servletContext = servletContext;
	}

	public void commit(FilterContext filterContext) {
        WintCookie wintCookie = sessionStore.commitForCookie();
        if (wintCookie == null) {
            return;
        }
        WintSessionHttpServletResponse response = (WintSessionHttpServletResponse) filterContext.getAttribute(AttrKeys.NEW_RESPONSE);
        response.addWintCookie(wintCookie);
	}

	public long getCreationTime() {
        Long value = sessionStore.getLong(SpecSessionKeys.CREATE_TIME);
        if (value == null) {
            return 0L;
        }
		return value;
	}

	public String getId() {
		return sessionStore.getSessionId();
	}

	public long getLastAccessedTime() {
        Long value = sessionStore.getLong(SpecSessionKeys.LAST_ACCESSED_TIME);
        if (value == null) {
            return 0L;
        }
        return value;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setMaxInactiveInterval(int interval) {
        sessionStore.setData(SpecSessionKeys.MAX_INACTIVE_INTERVAL, interval);
	}

	public int getMaxInactiveInterval() {
        Integer value = sessionStore.getInteger(SpecSessionKeys.MAX_INACTIVE_INTERVAL);
        if (value == null) {
            return 0;
        }
        return value;
	}

	public HttpSessionContext getSessionContext() {
		throw new UnsupportedOperationException();
	}

	public Object getAttribute(String name) {
		return sessionStore.getData(name);
	}

	public Object getValue(String name) {
		return getAttribute(name);
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getAttributeNames() {
		return Collections.enumeration(sessionStore.getNames());
	}

	@SuppressWarnings("unchecked")
	public String[] getValueNames() {
		return (String[])Collections.list(getAttributeNames()).toArray(new String[0]);
	}

	public void setAttribute(String name, Object value) {
		sessionStore.setData(name, value);
	}

	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}

	public void removeAttribute(String name) {
		sessionStore.remove(name);
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
