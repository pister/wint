package wint.sessionx.servlet;

import wint.lang.utils.StringUtil;
import wint.lang.utils.UrlUtil;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.store.SessionStore;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

@SuppressWarnings("deprecation")
public class WintSessionHttpServletRequest extends HttpServletRequestWrapper implements HttpServletRequest {

	private WintHttpSession wintHttpSession;

	private ServletContext servletContext;

    private SessionStore sessionStore;

    private String scheme;

    private Integer serverPort;

    private Boolean secure;

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

    public String getScheme() {
        if (StringUtil.isEmpty(scheme)) {
            return super.getScheme();
        }
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public int getServerPort() {
        if (serverPort == null) {
            return super.getServerPort();
        }
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public boolean isSecure() {
        if (secure == null) {
            return super.isSecure();
        }
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override
    public StringBuffer getRequestURL() {
        final StringBuffer url = new StringBuffer(48);
        String scheme = getScheme();
        int port = getServerPort();

        url.append(scheme);
        url.append("://");
        url.append(getServerName());
        if (port > 0 && ((scheme.equalsIgnoreCase(UrlUtil.HTTP) && port != UrlUtil.DEFAULT_HTTP_PORT)
                || (scheme.equalsIgnoreCase(UrlUtil.HTTPS) && port != UrlUtil.DEFAULT_HTTPS_PORT)))
        {
            url.append(':');
            url.append(port);
        }
        url.append(getRequestURI());
        return url;
      //  return super.getRequestURL();
    }
}
