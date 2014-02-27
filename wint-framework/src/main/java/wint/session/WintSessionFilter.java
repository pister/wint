package wint.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wint.mvc.servlet.ServletUtil;
import wint.session.WintSessionProcessor.ProcessorHandler;

/**
 * @deprecated use wint.sessionx.* instead !
 */
public class WintSessionFilter implements Filter {

	private WintSessionProcessor wintSessionProcessor = new WintSessionProcessor();
	
	public void init(FilterConfig filterConfig) throws ServletException {
		wintSessionProcessor.init(ServletUtil.getInitParameters(filterConfig), filterConfig.getServletContext());
	}
	
	static class FilterProcessorHandler implements ProcessorHandler {

		private FilterChain chain;
		
		public FilterProcessorHandler(FilterChain chain) {
			super();
			this.chain = chain;
		}

		public void onProcess(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
			chain.doFilter(httpRequest, httpResponse);
		}
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		wintSessionProcessor.process(httpRequest, httpResponse, new FilterProcessorHandler(chain));
	}

	public void destroy() {
		wintSessionProcessor.destroy();
	}

}
