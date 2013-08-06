package wint.mvc.init;

import javax.servlet.ServletContext;

/**
 * @author pister
 * 2012-1-11 02:37:30
 */
public class ServletContextLogger implements InitializeLogger {

	private ServletContext servletContext;
	
	public ServletContextLogger(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	public void log(String message) {
		servletContext.log(message);
	}
	
	public void log(String message, Throwable t) {
		servletContext.log(message, t);
	}

}
