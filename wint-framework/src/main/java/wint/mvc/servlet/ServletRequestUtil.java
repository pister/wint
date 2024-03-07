package wint.mvc.servlet;

import wint.lang.utils.CollectionUtil;
import wint.mvc.parameters.Parameters;
import wint.mvc.parameters.ServletParameters;
import wint.mvc.restful.request.NotSupportRequestBody;
import wint.mvc.restful.request.RequestBody;
import wint.mvc.restful.request.ServletRequestBody;

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
            return new NotSupportRequestBody("not support request body, please check your request METHOD!");
        }
        return new ServletRequestBody(httpServletRequest);
    }

    private static boolean supportRequestBody(HttpServletRequest httpServletRequest) {
        String method = httpServletRequest.getMethod();
        return supportRequestBodyMethods.contains(method.toUpperCase());
    }

}
