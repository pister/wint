package wint.mvc.holder;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wint.core.service.ServiceContext;
import wint.lang.magic.MagicMap;
import wint.mvc.flow.FlowData;

/**
 * @author pister
 * 2012-2-6 05:04:02
 */
public class WintContext {
	
	private static final ThreadLocal<MagicMap> variables = new ThreadLocal<MagicMap>();
	
	private static ServiceContext serviceContext;
	
	public static HttpServletResponse getResponse() {
		return (HttpServletResponse)getVariable(WinContextNames.RESPONSE);
	}
	
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest)getVariable(WinContextNames.REQUEST);
	}
	
	public static FlowData getFlowData() {
		return (FlowData)getVariable(WinContextNames.FLOW_DATA);
	}
	
	public static Locale getLocale() {
		return (Locale)getVariable(WinContextNames.LOCALE);
	}
	
	public static ServiceContext getServiceContext() {
		return serviceContext;
	}
	
	public static void setServiceContext(ServiceContext serviceContext) {
		WintContext.serviceContext = serviceContext;
	}

	public static void setVariable(String name, Object value) {
		getVariables().put(name, value);
	}
	
	public static Object getVariable(String name) {
		return getVariable(name, null);
	}
	
	private static MagicMap getVariables() {
		MagicMap m = variables.get();
		if (m == null) {
			m = MagicMap.newMagicMap();
			variables.set(m);
		}
		return m;
	}
	
	public static Object getVariable(String name, Object defaultValue) {
		MagicMap m = getVariables();
		if (m.containsKey(name)) {
			return m.get(name);
		}
		return defaultValue;
	}
	
	public static void clear() {
		variables.remove();
	}

}
