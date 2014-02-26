package wint.sessionx.servlet;

import wint.sessionx.filter.FilterContext;
import wint.sessionx.store.SessionStore;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class WintSessionHttpServletRequest extends HttpServletRequestWrapper implements HttpServletRequest {

	private WintHttpSession wintHttpSession;

	private ServletContext servletContext;

    private SessionStore sessionStore;

	public WintSessionHttpServletRequest(HttpServletRequest request, ServletContext servletContext, SessionStore sessionStore) {
		super(request);
		this.servletContext = servletContext;
        this.sessionStore = sessionStore;
	}

	private void createSession() {
		WintHttpSession wintHttpSession = new WintHttpSession(sessionStore, servletContext);
		this.wintHttpSession = wintHttpSession;
	}
	
	public void commit(FilterContext filterContext) {
		if (wintHttpSession != null) {
			wintHttpSession.commit(filterContext);
		}
	}
	
	@Override
	public HttpSession getSession(boolean create) {
		// 这里线程未做同步
		if (wintHttpSession != null) {
			return wintHttpSession;
		}
		
		if (create) {
			createSession();
		}
		return wintHttpSession;
	}

	@Override
	public HttpSession getSession() {
		return getSession(true);
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return true;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return true;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

}
