package wint.mvc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wint.core.io.resource.loader.ResourceLoader;
import wint.core.io.resource.loader.WebAppResourceLoader;
import wint.lang.misc.profiler.Profiler;
import wint.mvc.init.DispatcherInitializor;
import wint.mvc.init.ServletContextLogger;
import wint.mvc.servlet.ServletUtil;

/**
 * @author pister 2012-3-5 07:44:14
 */
public class DispatcherFilter implements Filter {

	private Dispatcher dispatcher = new Dispatcher();
	
	public void destroy() {
		dispatcher.destroy();
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			Profiler.enter("DispatcherFilter doFilter");
			dispatcher.execute((HttpServletRequest) request, (HttpServletResponse) response);
		} finally {
			Profiler.release();
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext servletContext = filterConfig.getServletContext();
		ResourceLoader resourceLoader = new WebAppResourceLoader(filterConfig.getServletContext());
		DispatcherInitializor dispatcherInitializor = new DispatcherInitializor(resourceLoader, ServletUtil.getInitParameters(filterConfig), new ServletContextLogger(servletContext), servletContext);
		dispatcher.init(dispatcherInitializor);
	}

}
