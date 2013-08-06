package wint.mvc.flow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wint.core.service.Service;

/**
 * @author pister
 * 2012-1-11 02:37:48
 */
public interface FlowDataService extends Service {
	
	InnerFlowData createFlowData(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

}
