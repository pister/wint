package wint.help.mvc.security.csrf;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

import wint.help.codec.MD5;
import wint.mvc.flow.FlowData;
import wint.mvc.flow.Session;

/**
 * 在用户会话期间内是不变的，好处是用户可以再任意页面同时使用
 * 坏处就是不能防止重复提交
 * @author pister
 * 2012-9-9 下午03:59:04
 */
/**
 * @author pister
 * 2012-9-9 下午04:13:28
 */
public class FixedBySessionCsrfTokenGenerator extends AbstractCsrfTokenGenerator implements CsrfTokenGenerator {

	private AtomicLong seq = new AtomicLong(1);
	
	public String currentToken(FlowData flowData, String groupName, String tokenName) {
		return getTokensFromSession(flowData, groupName, tokenName);
	}

	public String nextToken(FlowData flowData, String groupName, String tokenName) {
		return getTokensFromSession(flowData, groupName, tokenName);
	}
	
	
	protected String getTokenName(String groupName, String tokenName) {
		return TokenNameUtil.makeTokenName("x", tokenName);
	}

	private String getTokensFromSession(FlowData flowData, String groupName, String tokenName) {
		String targetTokenName = getTokenName(groupName, tokenName);
		Session session = flowData.getSession();
		String token = (String)session.getAttribute(targetTokenName);
		if (token == null) {
			token = genTokenInner();
			session.setAttribute(targetTokenName, token);
		}
		return token;
	}
	
	private String genTokenInner() {
		String value = MD5.encrypt(UUID.randomUUID().toString());
		if (value.length() > 15) {
			value = value.substring(0, 15);
		}
		long longValue = Long.parseLong(value, 16);
		return Long.toString(seq.incrementAndGet(), 36) + "_" + Long.toString(longValue, 36);
	}
	
	
}
