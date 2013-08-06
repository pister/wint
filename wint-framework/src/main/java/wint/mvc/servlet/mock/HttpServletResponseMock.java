package wint.mvc.servlet.mock;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import wint.lang.magic.MagicMap;
import wint.lang.utils.CollectionUtil;

public class HttpServletResponseMock implements HttpServletResponse {

	private MagicMap headers = MagicMap.newMagicMap();
	
	private List<Cookie> cookies = CollectionUtil.newArrayList();
	
	private String characterEncoding;
	
	private int bufferSize = 8 * 1024;
	
	public void addCookie(Cookie cookie) {
		cookies.add(cookie);
	}

	public MagicMap getHeaders() {
		return headers;
	}

	public void addDateHeader(String name, long date) {
		headers.put(name, date);
	}

	public void addHeader(String name, String value) {
		headers.put(name, value);
	}

	public void addIntHeader(String name, int value) {
		headers.put(name, value);
	}

	public boolean containsHeader(String name) {
		return headers.containsKey(name);
	}

	public String encodeRedirectURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public String encodeRedirectUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public String encodeURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public String encodeUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendError(int sc) throws IOException {
		// TODO Auto-generated method stub

	}

	public void sendError(int sc, String msg) throws IOException {
		// TODO Auto-generated method stub

	}

	public void sendRedirect(String location) throws IOException {
		// TODO Auto-generated method stub

	}

	public void setDateHeader(String name, long date) {
		// TODO Auto-generated method stub

	}

	public void setHeader(String name, String value) {
		// TODO Auto-generated method stub

	}

	public void setIntHeader(String name, int value) {
		// TODO Auto-generated method stub

	}

	public void setStatus(int sc) {
		// TODO Auto-generated method stub

	}

	public void setStatus(int sc, String sm) {
		// TODO Auto-generated method stub

	}

	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub

	}

	public int getBufferSize() {
		return bufferSize;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	public ServletOutputStream getOutputStream() throws IOException {
		return new ServletOutputStreamMock();
	}

	public PrintWriter getWriter() throws IOException {
		return new PrintWriterMock();
	}

	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	public void reset() {
		// TODO Auto-generated method stub

	}

	public void resetBuffer() {
		// TODO Auto-generated method stub

	}

	public void setBufferSize(int size) {
		this.bufferSize = size;
	}

	public void setCharacterEncoding(String charset) {
		this.characterEncoding = charset;
	}

	public void setContentLength(int len) {
		// TODO Auto-generated method stub

	}

	public void setContentType(String type) {
		// TODO Auto-generated method stub

	}

	public void setLocale(Locale loc) {
		// TODO Auto-generated method stub

	}

}
