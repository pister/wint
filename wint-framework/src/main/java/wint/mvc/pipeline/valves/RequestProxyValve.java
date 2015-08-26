package wint.mvc.pipeline.valves;

import wint.lang.utils.UrlUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.holder.WintContext;
import wint.mvc.pipeline.PipelineContext;
import wint.sessionx.servlet.WintSessionHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * User: huangsongli
 * Date: 15/8/20
 * Time: 下午1:50
 */
public class RequestProxyValve extends AbstractValve {

    private static final String PROTOCOL_HEADER = "X-Forwarded-Proto";

    private static final String SERVER_PORT_NAME = "X-Server-Port";

    @Override
    public void invoke(PipelineContext pipelineContext, InnerFlowData innerFlowData) {
        HttpServletRequest request = WintContext.getRequest();
        if (!(request instanceof WintSessionHttpServletRequest)) {
            pipelineContext.invokeNext(innerFlowData);
        } else {
            WintSessionHttpServletRequest wintRequest = (WintSessionHttpServletRequest)request;
            String protocolHeaderValue = wintRequest.getHeader(PROTOCOL_HEADER);
            String serverPort = wintRequest.getHeader(SERVER_PORT_NAME);
            Integer serverPortInteger = null;
            if (serverPort != null) {
                serverPortInteger = Integer.valueOf(serverPort);
            }
            if (protocolHeaderValue == null) {
                // don't modify the secure,scheme and serverPort attributes
                // of the request
            } else if (UrlUtil.HTTPS.equalsIgnoreCase(protocolHeaderValue)) {
                wintRequest.setScheme(protocolHeaderValue);
                wintRequest.setSecure(true);
                if (serverPortInteger == null) {
                    serverPortInteger = UrlUtil.DEFAULT_HTTPS_PORT;
                }
                wintRequest.setServerPort(serverPortInteger);
            } else {
                wintRequest.setSecure(false);
                wintRequest.setScheme(UrlUtil.HTTP);
                if (serverPortInteger != null) {
                    wintRequest.setServerPort(serverPortInteger);
                }
            }
            pipelineContext.invokeNext(innerFlowData);
        }


    }
}
