package wint.session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import junit.framework.TestCase;
import wint.core.config.Constants;
import wint.lang.magic.MagicMap;
import wint.lang.utils.FileUtil;
import wint.mvc.DispatcherServlet;
import wint.mvc.servlet.mock.FilterConfigMock;
import wint.mvc.servlet.mock.HttpServletRequestMock;
import wint.mvc.servlet.mock.HttpServletResponseMock;
import wint.mvc.servlet.mock.ServletConfigMock;
import wint.mvc.servlet.mock.SimpleServletFilterChainMock;
import wint.sessionx.WintSessionFilter;
import wint.sessionx.cookie.CookieUtil;

public class SessionFilterTests extends TestCase {

	private DispatcherServlet dispatcherServlet;
	
	private ServletConfig servletConfigMock;
	
	private FilterConfigMock filterConfigMock;
	
	private WintSessionFilter wintSessionFilter;
	
	private File tempCookieFile;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dispatcherServlet = new DispatcherServlet();
		
		wintSessionFilter = new WintSessionFilter();
		
		MagicMap initParameters = MagicMap.newMagicMap();
		initParameters.put(Constants.PropertyKeys.APP_PACKAGE, "wint.demo.app");
		initParameters.put(Constants.PropertyKeys.TEMPLATE_PATH, "test_template");
		servletConfigMock = new ServletConfigMock(initParameters, initParameters);
		
		filterConfigMock = new FilterConfigMock("myFilter", servletConfigMock.getServletContext());
		
		wintSessionFilter.init(filterConfigMock);
		
		dispatcherServlet.init(servletConfigMock);
		
		String userHome = System.getProperty("user.home");
		File userHomeDir = new File(userHome);
		tempCookieFile = new File(userHomeDir, "win_temp_cookie5");
		if (!tempCookieFile.exists()) {
			FileOutputStream fos = new FileOutputStream(tempCookieFile);
			fos.close();
		}
	}
	
	public void testWintSession() throws ServletException, IOException {
		String requestCookie = FileUtil.readAsString(tempCookieFile);
		System.out.println("=======================request cookies=======================");
		System.out.println(requestCookie);
		System.out.println("=============================================================");
		MagicMap parameters = MagicMap.newMagicMap();
		
		
		HttpServletRequestMock request = new HttpServletRequestMock("session-foo/first", parameters, servletConfigMock.getServletContext());
		HttpServletResponseMock response = new HttpServletResponseMock();
	
		request.getHeaders().put("Cookie", requestCookie);
		SimpleServletFilterChainMock filterChainMock = new SimpleServletFilterChainMock(dispatcherServlet);
		wintSessionFilter.doFilter(request, response, filterChainMock);
		
		MagicMap headers  = response.getHeaders();
		String cookies = headers.getString(CookieUtil.SET_COOKIE);
		System.out.println(cookies);
		FileUtil.writeContent(tempCookieFile, cookies);
		
	}
	
	
}
