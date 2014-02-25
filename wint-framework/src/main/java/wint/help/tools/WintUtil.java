package wint.help.tools;

import wint.mvc.flow.FlowData;
import wint.mvc.holder.WintContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: longyi
 * Date: 14-2-25
 * Time: 下午7:46
 */
public class WintUtil {

    public static FlowData getFlowData() {
        return WintContext.getFlowData();
    }

    public static HttpServletRequest getRequest() {
        return WintContext.getRequest();
    }

    public static HttpServletResponse getResponse() {
        return WintContext.getResponse();
    }

    public static Object getSession(String name) {
        return WintContext.getFlowData().getSession().getAttribute(name);
    }

    public static String getHeader(String name) {
        return WintContext.getRequest().getHeader(name);
    }
}
