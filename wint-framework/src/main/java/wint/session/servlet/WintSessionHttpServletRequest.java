package wint.session.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import wint.session.config.SessionConfig;

public class WintSessionHttpServletRequest extends HttpServletRequestWrapper implements HttpServletRequest {

	private WintSessionHttpServletResponse wintSessionHttpServletResponse;
	
	private WintHttpSession wintHttpSession;
	
	private ServletContext servletContext;
	
	private SessionConfig sessionConfig;
	
	public WintSessionHttpServletRequest(HttpServletRequest request, ServletContext servletContext, SessionConfig sessionConfig, WintSessionHttpServletResponse wintSessionHttpServletResponse) {
		super(request);
		this.servletContext = servletContext;
		this.sessionConfig = sessionConfig;
		this.wintSessionHttpServletResponse = wintSessionHttpServletResponse;
	}

	public void setWintHttpSession(WintHttpSession wintHttpSession) {
		this.wintHttpSession = wintHttpSession;
	}

	private void createSession() {
		WintHttpSession wintHttpSession = new WintHttpSession(this, wintSessionHttpServletResponse);
		wintHttpSession.init(servletContext, sessionConfig);
		this.wintHttpSession = wintHttpSession;
	}
	
	public void commit() {
		if (wintHttpSession != null) {
			wintHttpSession.commit();
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
