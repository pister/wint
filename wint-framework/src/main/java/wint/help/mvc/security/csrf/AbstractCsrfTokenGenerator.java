package wint.help.mvc.security.csrf;

import wint.mvc.flow.FlowData;
import wint.mvc.flow.Session;

public abstract class AbstractCsrfTokenGenerator implements CsrfTokenGenerator {

    public void clearToken(FlowData flowData, String groupName, String tokenName) {
        String targetTokenName = getTokenName(groupName, tokenName);
        Session session = flowData.getSession();
        session.removeAttribute(targetTokenName);
    }

    protected abstract String getTokenName(String groupName, String tokenName);

}
