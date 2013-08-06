package wint.mvc.servlet.mock;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServletFilterChainMock implements FilterChain {
	
	private HttpServlet httpServlet;
	
	public SimpleServletFilterChainMock(HttpServlet httpServlet) {
		super();
		this.httpServlet = httpServlet;
	}

	public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		httpServlet.service((HttpServletRequest)request, (HttpServletResponse)response);
	}

}
