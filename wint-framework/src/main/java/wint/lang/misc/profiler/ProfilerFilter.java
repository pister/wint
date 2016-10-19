package wint.lang.misc.profiler;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ProfilerFilter implements Filter {

	private int profilerTime = 1000;
	
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			Profiler.start("start process...");
			chain.doFilter(request, response);			
		} finally {
			Profiler.release();
			Profiler.logMessage(profilerTime);
			Profiler.reset();
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		profilerTime = Integer.parseInt(filterConfig.getInitParameter("profilerTime"));
	}

}
