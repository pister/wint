package wint.mvc.servlet;

import java.util.Collections;
import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import wint.lang.magic.MagicMap;
import wint.lang.utils.StringUtil;
import wint.mvc.servlet.domains.DomainSuffixes;

/**
 * @author pister 2011-12-28 02:40:33
 */
public class ServletUtil {

    public static String getServletPathWithRequestContext(HttpServletRequest request, String requestContextPath) {
        String path = getServletPath(request);
        if (path.startsWith(requestContextPath)) {
            path = path.substring(requestContextPath.length());
        }
        return path;
    }


    public static String getRequestHostname(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        StringBuffer url = request.getRequestURL();
        url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
        String hostname = parseHostnameFromUrl(url.toString());
        return hostname;
    }

    public static String getRequestHostnameL2(HttpServletRequest request) {
        String hostname = getRequestHostname(request);
        return getHostnameL2(hostname);
    }

    public static String getHostnameL2(String hostname) {
        if (StringUtil.isEmpty(hostname)) {
            return null;
        }
        String suffix = getMatchedSuffix(hostname);
        if (suffix == null) {
            return null;
        }
        String name = hostname.substring(0, hostname.length() - suffix.length());
        int pos = name.indexOf(".");
        if (pos < 0) {
            return null;
        }
        return name.substring(0, pos);
    }

    private static String getMatchedSuffix(String hostname) {
        String lowerHostname = hostname.toLowerCase();
        for (String suffix : DomainSuffixes.SUFFIXES) {
            if (lowerHostname.endsWith(suffix)) {
                return suffix;
            }
        }
        return null;
    }


    static String parseHostnameFromUrl(String url) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }
        int pos = url.indexOf("://");
        if (pos < 0) {
            // 没有协议
            url = "://" + url;
        }

        url = trimPathAndQuery(url);
        int protocolPos = url.indexOf("://");
        String hostname = url.substring(protocolPos + 3);
        return StringUtil.getFirstBefore(hostname, ":");
    }

    private static String trimPathAndQuery(String url) {
        int protocolPos = url.indexOf("://");
        int pathIndex = url.indexOf("/", protocolPos + 3);
        if (pathIndex < 0) {
            // 没有path
            int queryStringIndex = url.indexOf("?",  protocolPos + 3);
            if (queryStringIndex < 0) {
                return url;
            }
            return url.substring(0, queryStringIndex);
        } else {
            return url.substring(0, pathIndex);
        }
    }


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
