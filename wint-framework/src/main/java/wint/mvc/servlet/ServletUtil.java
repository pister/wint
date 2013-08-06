package wint.mvc.servlet;

import java.util.Collections;
import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import wint.lang.magic.MagicMap;
import wint.lang.utils.StringUtil;

/**
 * @author pister 2011-12-28 02:40:33
 */
public class ServletUtil {

	public static String getServletPath(HttpServletRequest request) {
		final String pathInfo = request.getPathInfo();
		if (!StringUtil.isEmpty(pathInfo)) {
			return pathInfo;
		}
        String servletPath = request.getServletPath();
        String requestUri = request.getRequestURI();
        // Detecting other characters that the servlet container cut off (like anything after ';')
        if (requestUri != null && servletPath != null && !requestUri.endsWith(servletPath)) {
            int pos = requestUri.indexOf(servletPath);
            if (pos > -1) {
                servletPath = requestUri.substring(requestUri.indexOf(servletPath));
            }
        }
        if (null != servletPath && !"".equals(servletPath)) {
            return servletPath;
        }
        int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
        int endIndex = pathInfo == null ? requestUri.length() : requestUri.lastIndexOf(pathInfo);

        if (startIndex > endIndex) { // this should not happen
            endIndex = startIndex;
        }

        return requestUri.substring(startIndex, endIndex);
    }
	
	@SuppressWarnings("unchecked")
	public static MagicMap getInitParameters(ServletConfig servletConfig) {
		MagicMap magicMap = MagicMap.newMagicMap();
		initServletContextParameters(magicMap, servletConfig.getServletContext());
		for (String name : (List<String>)Collections.list(servletConfig.getInitParameterNames())) {
			String value = servletConfig.getInitParameter(name);
			magicMap.put(name, value);
		}
		return magicMap;
	}
	
	@SuppressWarnings("unchecked")
	private static void initServletContextParameters(MagicMap magicMap, ServletContext servletContext) {
		for (String name : (List<String>)Collections.list(servletContext.getInitParameterNames())) {
			String value = servletContext.getInitParameter(name);
			magicMap.put(name, value);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static MagicMap getInitParameters(FilterConfig filterConfig) {
		MagicMap magicMap = MagicMap.newMagicMap();
		initServletContextParameters(magicMap, filterConfig.getServletContext());
		for (String name : (List<String>)Collections.list(filterConfig.getInitParameterNames())) {
			String value = filterConfig.getInitParameter(name);
			magicMap.put(name, value);
		}
		return magicMap;
	}

}
