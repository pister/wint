package wint.help.mvc.security.csrf;

import wint.mvc.flow.FlowData;

public interface CsrfTokenGenerator {
	
	String nextToken(FlowData flowData, String groupName, String tokenName);

	String currentToken(FlowData flowData, String groupName, String tokenName);
	
	void clearToken(FlowData flowData, String groupName, String tokenName);
}
