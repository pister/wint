package wint.mvc.servlet.mock;

import org.springframework.mock.web.DelegatingServletInputStream;
import wint.lang.io.FastByteArrayInputStream;
import wint.lang.magic.MagicMap;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;
import wint.sessionx.cookie.CookieUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HttpServletRequestMock implements HttpServletRequest {

    private MagicMap headers = MagicMap.newMagicMap();

    private MagicMap parameters = MagicMap.newMagicMap();

    private String method = "GET";

    private String pathInfo;

    private String charsetEncoding = "utf-8";

    private MagicMap attributes = MagicMap.newMagicMap();

    private HttpSession httpSession;

    private byte[] body;

    private static final String COOKIE_DATE_PATTERN = "EEE, dd-MMM-yyyy HH:mm:ss 'GMT'";

    public HttpServletRequestMock(String pathInfo, MagicMap parameters, ServletContext servletContext) {
        super();
        this.pathInfo = pathInfo;
        this.parameters = parameters;
        this.httpSession = new HttpSessionMock(servletContext);

    }

    public String getAuthType() {
        return null;
    }

    public String getContextPath() {
        return pathInfo;
    }

    private int getMaxAge(String source) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(COOKIE_DATE_PATTERN, Locale.US);
            Date date = sdf.parse(source);
            long ts = (date.getTime() - (new Date()).getTime());
            return (int) (ts / 1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Cookie[] getCookies() {
        String cookies = headers.getString(CookieUtil.COOKIE);
        if (cookies == null) {
            return null;
        }
        List<String> stringCookies = StringUtil.splitTrim(cookies, ";");
        List<Cookie> ret = CollectionUtil.newArrayList();

        Cookie lastCookie = null;
        for (String stringCookie : stringCookies) {
            String name = StringUtil.getFirstBefore(stringCookie, "=");
            String value = StringUtil.getLastAfter(stringCookie, "=");

            if (CookieUtil.EXPIRES.equals(name)) {
                if (lastCookie == null) {
                    continue;
                }
                lastCookie.setMaxAge(getMaxAge(value));
                continue;
            }
            if (CookieUtil.HTTP_ONLY.equals(name)) {
                // 会略httpOnly
                continue;
            }
            if (CookieUtil.DOMAIN.equals(name)) {
                // 会略httpOnly
                continue;
            }

            if (CookieUtil.PATH.equals(name)) {
                if (lastCookie == null) {
                    continue;
                }
                lastCookie.setPath(value);
            }

            Cookie cookie = new Cookie(name, value);
            ret.add(cookie);
        }
        return ret.toArray(new Cookie[0]);
    }

    public long getDateHeader(String name) {
        return headers.getLong(name);
    }

    public String getHeader(String name) {
        return headers.getString(name);
    }


    public Enumeration<?> getHeaderNames() {
        return Collections.enumeration(headers.keySet());
    }

    public Enumeration<?> getHeaders(String name) {
        return null;
    }

    public int getIntHeader(String name) {
        return headers.getInt(name);
    }

    public MagicMap getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getPathTranslated() {
        return null;
    }

    public String getQueryString() {
        return parameters.join("=", "&");
    }

    public String getRemoteUser() {
        return null;
    }

    public String getRequestURI() {
        return pathInfo;
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public String getRequestedSessionId() {
        return null;
    }

    public String getServletPath() {
        return pathInfo;
    }

    public HttpSession getSession() {
        return httpSession;
    }

    public HttpSession getSession(boolean create) {
        return httpSession;
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isUserInRole(String role) {
        return false;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration<?> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    public String getCharacterEncoding() {
        return charsetEncoding;
    }

    public int getContentLength() {
        return 0;
    }

    public String getContentType() {
        return null;
    }

    public ServletInputStream getInputStream() throws IOException {
        if (body == null) {
            return null;
        }
        return new DelegatingServletInputStream(new FastByteArrayInputStream(body));
    }

    public String getLocalAddr() {
        return null;
    }

    public String getLocalName() {
        return null;
    }

    public int getLocalPort() {
        return 0;
    }

    public Locale getLocale() {
        // TODO， 正确的方法是解析类似的 文本 zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4
        String lang = this.headers.getString("Accept-Language");
        if (lang == null) {
            return null;
        }
        if (lang.contains("zh")) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        if (lang.contains("en")) {
            if (lang.contains("US")) {
                return Locale.US;
            } else {
                return Locale.ENGLISH;
            }
        }
        // othe not support
        return null;
    }

    public Enumeration<?> getLocales() {
        return null;
    }

    public String getParameter(String name) {
        return parameters.getString(name);
    }

    public Map<?, ?> getParameterMap() {
        return Collections.unmodifiableMap(parameters);
    }

    public Enumeration<?> getParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }

    public String[] getParameterValues(String name) {
        return new String[]{parameters.getString(name)};
    }

    public String getProtocol() {
        return null;
    }

    public BufferedReader getReader() throws IOException {
        if (body == null) {
            return null;
        }
        return new BufferedReader(new StringReader(new String(body, StandardCharsets.UTF_8)));
    }

    public String getRealPath(String path) {
        return null;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public int getRemotePort() {
        return 0;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    public String getScheme() {
        return null;
    }

    public String getServerName() {
        return null;
    }

    public int getServerPort() {
        return 0;
    }

    public boolean isSecure() {
        return false;
    }

    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    public void setAttribute(String name, Object o) {
        attributes.put(name, o);
    }

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        this.charsetEncoding = env;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
