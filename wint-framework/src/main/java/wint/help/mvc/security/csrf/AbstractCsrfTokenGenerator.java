package wint.help.mvc.security.csrf;

import javax.servlet.http.HttpSession;

import wint.mvc.flow.FlowData;

public abstract class AbstractCsrfTokenGenerator implements CsrfTokenGenerator {

	public void clearToken(FlowData flowData, String groupName, String tokenName) {
		String targetTokenName = getTokenName(groupName, tokenName);
		HttpSession session = flowData.getSession();
		session.removeAttribute(targetTokenName);
	}
	
	protected abstract String getTokenName(String groupName, String tokenName);

}
