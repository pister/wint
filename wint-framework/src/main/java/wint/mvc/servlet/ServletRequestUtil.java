package wint.mvc.servlet;

import wint.lang.utils.CollectionUtil;
import wint.mvc.parameters.Parameters;
import wint.mvc.parameters.ServletParameters;
import wint.mvc.request.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Set;

/**
 * @author songlihuang
 * @date 2024/3/7 09:31
 */
public class ServletRequestUtil {

    private static final Set<String> supportRequestBodyMethods = CollectionUtil.newHashSet(Arrays.asList("POST", "PUT", "PATCH"));

    public static Parameters createParameters(HttpServletRequest httpServletRequest) {
        return new ServletParameters(httpServletRequest);
    }

    public static RequestBody createRequestBody(HttpServletRequest httpServletRequest) {
        if (!supportRequestBody(httpServletRequest)) {
            return new NotSupportRequestBody("Use getRequestBody() must use one of POST/PUT/PATCH method, please check your request method!");
        }
        return new ServletRequestBody(httpServletRequest);
    }

    public static RequestHeaders createRequestHeaders(HttpServletRequest httpServletRequest) {
        return new ServletRequestHeaders(httpServletRequest);
    }

    private static boolean supportRequestBody(HttpServletRequest httpServletRequest) {
        String method = httpServletRequest.getMethod();
        return supportRequestBodyMethods.contains(method.toUpperCase());
    }

}
