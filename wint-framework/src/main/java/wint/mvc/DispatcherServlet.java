package wint.mvc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wint.core.io.resource.loader.ResourceLoader;
import wint.core.io.resource.loader.WebAppResourceLoader;
import wint.mvc.init.DispatcherInitializor;
import wint.mvc.init.ServletContextLogger;
import wint.mvc.servlet.ServletUtil;

/**
 * @author pister 2012-3-5 07:44:18
 */
public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = -5755961688608232773L;

	private Dispatcher dispatcher = new Dispatcher();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		ResourceLoader resourceLoader = new WebAppResourceLoader(servletContext);
		DispatcherInitializor dispatcherInitializor = new DispatcherInitializor(resourceLoader, ServletUtil.getInitParameters(config), new ServletContextLogger(servletContext), servletContext);
		dispatcher.init(dispatcherInitializor);
	}

	@Override
	public void destroy() {
		dispatcher.destroy();
		super.destroy();
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dispatcher.execute(request, response);
	}

}
