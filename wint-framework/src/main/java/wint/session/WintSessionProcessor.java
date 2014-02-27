package wint.session;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wint.core.config.Constants;
import wint.lang.magic.MagicMap;
import wint.session.config.DefaultSessionConfig;
import wint.session.config.SessionConfig;
import wint.session.servlet.WintSessionHttpServletRequest;
import wint.session.servlet.WintSessionHttpServletResponse;

/**
 * @deprecated use wint.sessionx.* instead !
 */
public class WintSessionProcessor {
	
	private SessionConfig sessionConfig;
	
	private ServletContext servletContext;
	
	private String charset;
	
	public void init(MagicMap initParamters, ServletContext servletContext) {
		DefaultSessionConfig sessionConfig = new DefaultSessionConfig();
		sessionConfig.init(initParamters);
		
		this.sessionConfig = sessionConfig;
		this.servletContext = servletContext;
		this.charset = initParamters.getString(Constants.PropertyKeys.CHARSET_ENCODING, Constants.Defaults.CHARSET_ENCODING);
		servletContext.log("Wint Session has been initialized.");
	}
	
	public void destroy() {
		
	}
	
	public void process(HttpServletRequest httpRequest, HttpServletResponse httpResponse, ProcessorHandler processorHandler) throws ServletException, IOException {
		WintSessionHttpServletResponse wintSessionHttpServletResponse = new WintSessionHttpServletResponse(httpResponse, charset);
		WintSessionHttpServletRequest wintSessionHttpServletRequest = new WintSessionHttpServletRequest(httpRequest,servletContext, sessionConfig, wintSessionHttpServletResponse);
		try {
			processorHandler.onProcess(wintSessionHttpServletRequest, wintSessionHttpServletResponse);
		} finally {
			// 注意顺序，先提交request,然后提交response
			wintSessionHttpServletRequest.commit();
			wintSessionHttpServletResponse.commit();
		}
	}
	
	public static interface ProcessorHandler {
		void onProcess(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException;
	}

}
