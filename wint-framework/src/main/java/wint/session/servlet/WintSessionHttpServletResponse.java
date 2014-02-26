package wint.session.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import wint.core.config.Constants;
import wint.lang.io.FastByteArrayOutputStream;
import wint.lang.utils.CollectionUtil;
import wint.session.cookie.WintCookie;
import wint.session.util.CookieUtil;

public class WintSessionHttpServletResponse extends HttpServletResponseWrapper implements HttpServletResponse {

	private List<String> cookieHeaders = CollectionUtil.newArrayList(); 

	private List<Cookie> cookies = CollectionUtil.newArrayList();
	
	private String charset;

	private boolean sendRedirected = false;

	private String redirectLocation;

	private FastByteArrayOutputStream fastByteArrayOutputStream;

	private boolean hasGetOutputStream = false;

	private boolean hasGetWriter = false;

	private boolean commit = false;

	public WintSessionHttpServletResponse(HttpServletResponse response) {
		this(response, Constants.Defaults.CHARSET_ENCODING);
	}
	public WintSessionHttpServletResponse(HttpServletResponse response, String charset) {
		super(response);
		this.charset = charset;
	}
	
	// 2、处理http协议
	// add cookie, write outputstream, for sendRedirect, header status(301/302)
	public void commit() {
		if (commit) {
			return;
		}
		try {
			// cookieHeaders
			if (!CollectionUtil.isEmpty(cookieHeaders)) {
				for (String cookieValue : cookieHeaders) {
					super.addHeader(CookieUtil.SET_COOKIE, cookieValue);
				}
			}
			
			// add cookie
			for (Cookie cookie : cookies) {
				super.addCookie(cookie);
			}

			// for redirect
			if (sendRedirected) {
				super.sendRedirect(redirectLocation);
			}

			// for response data
			if (fastByteArrayOutputStream != null) {
				OutputStream os = super.getOutputStream();
				os.write(fastByteArrayOutputStream.toByteArray());
				os.flush();
				os.close();
			}

			commit = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addCookie(Cookie cookie) {
		addWintCookie(cookie);
	}

	public void addWintCookie(Cookie cookie) {
		String cookieString;
		if (cookie instanceof WintCookie) {
			cookieString = CookieUtil.buildCookieString(cookie, ((WintCookie)cookie).isHttpOnly());
		} else {
			cookieString = CookieUtil.buildCookieString(cookie, false);
		}
		cookieHeaders.add(cookieString);
	}
	
	
	@Override
	public void addHeader(String name, String value) {
		if (CookieUtil.SET_COOKIE.equals(name)) {
			// cookieHeaders
			cookieHeaders.add(value);
		} else {
			super.addHeader(name, value);
		}
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		sendRedirected = true;
		this.redirectLocation = location;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (hasGetWriter) {
			throw new IllegalStateException("getOutputStream() and getWriter() are not allowed call both.");
		}
		DelegatingServletOutputStream ret = new DelegatingServletOutputStream(getOutputStreamBuffer());
		hasGetOutputStream = true;
		return ret;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (hasGetOutputStream) {
			throw new IllegalStateException("getOutputStream() and getWriter() are not allowed call both.");
		}
		PrintWriter ret = new PrintWriter(new OutputStreamWriter(getOutputStreamBuffer(), charset));
		hasGetWriter = true;
		return ret;
	}

	private FastByteArrayOutputStream getOutputStreamBuffer() {
		if (fastByteArrayOutputStream != null) {
			return fastByteArrayOutputStream;
		}
		fastByteArrayOutputStream = new FastByteArrayOutputStream(getBufferSize());
		return fastByteArrayOutputStream;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (fastByteArrayOutputStream != null) {
			fastByteArrayOutputStream.flush();
		}
	}

	@Override
	public boolean isCommitted() {
		return commit;
	}

	@Override
	public void reset() {
		if (commit) {
			throw new IllegalStateException("response has been committed!");
		}
		resetBuffer();
		super.reset();
	}

	@Override
	public void resetBuffer() {
		if (commit) {
			throw new IllegalStateException("response has been committed!");
		}
		fastByteArrayOutputStream = null;
		hasGetOutputStream = false;
		hasGetWriter = false;
	}

}
