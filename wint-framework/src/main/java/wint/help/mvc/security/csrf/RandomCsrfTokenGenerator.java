package wint.help.mvc.security.csrf;

import java.util.List;

import javax.servlet.http.HttpSession;

import wint.lang.magic.MagicList;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.SecureRandomStringUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;

/**
 * 随机生成的token，好处就是可以防止重复提交
 * @author pister
 * 2012-9-9 下午03:58:20
 */
public class RandomCsrfTokenGenerator extends AbstractCsrfTokenGenerator implements CsrfTokenGenerator {

	private static final int TOKEN_BUFFER_COUNT = 3;
	
	private static final int TOKEN_PART1_LENGTH = 4;
	
	private static final int TOKEN_PART2_LENGTH = 6;
	
	public String currentToken(FlowData flowData, String groupName, String tokenName) {
		List<String> tokens = getTokensFromSession(flowData, groupName, tokenName);
		return tokens.get(0);
	}
	
	public String nextToken(FlowData flowData, String groupName, String tokenName) {
		List<String> tokens = getTokensFromSession(flowData, groupName, tokenName);
		HttpSession session = flowData.getSession();
		tokenName = TokenNameUtil.makeTokenName(groupName, tokenName);
		if (tokens.size() == 1) {
			tokens = createTokens();
			session.setAttribute(tokenName, CollectionUtil.join(tokens, ","));
			return tokens.get(0);
		} else {
			tokens.remove(0);
			session.setAttribute(tokenName, CollectionUtil.join(tokens, ","));
			return tokens.get(0);
		}
	}
	
	protected String getTokenName(String groupName, String tokenName) {
		return TokenNameUtil.makeTokenName(groupName, tokenName);
	}
	
	private List<String> getTokensFromSession(FlowData flowData, String groupName, String tokenName) {
		String targetTokenName = getTokenName(groupName, tokenName);
		HttpSession session = flowData.getSession();
		String tokensString = (String)session.getAttribute(targetTokenName);
		List<String> tokens = toTokens(tokensString);
		if (CollectionUtil.isEmpty(tokens)) {
			tokens = createTokens();
		}
		session.setAttribute(targetTokenName, CollectionUtil.join(tokens, ","));
		return tokens;
	}

	
	private static List<String> toTokens(String tokensString) {
		if (StringUtil.isEmpty(tokensString)) {
			return null;
		}
		String[] tokens = tokensString.split(",");
		if (tokens == null || tokens.length == 0) {
			return null;
		}
		return CollectionUtil.newLinkedList(tokens);
	}
	
	private static MagicList<String> createTokens() {
		MagicList<String> tokens = MagicList.newList(TOKEN_BUFFER_COUNT);
		String part1 = SecureRandomStringUtil.randomAlphanumeric(TOKEN_PART1_LENGTH);
		for (int i = 0; i < TOKEN_BUFFER_COUNT; ++i) {
			String part2 = SecureRandomStringUtil.randomAlphanumeric(TOKEN_PART2_LENGTH);
			String token = part1 + "_" + part2;
			tokens.add(token);
		}
		return tokens;
	}
}
