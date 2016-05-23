package wint.mvc.url.rewrite.domain;

import wint.lang.utils.StringUtil;
import wint.mvc.servlet.ServletUtil;
import wint.mvc.url.rewrite.resovler.RewriteResolver;

/**
 * User: huangsongli
 * Date: 16/5/23
 * Time: 下午5:10
 */
public class DefaultDomainParser implements DomainParser {

    private String parameterName;

    private RewriteResolver rewriteResolver;

    public DefaultDomainParser(String parameterName, RewriteResolver rewriteResolver) {
        this.parameterName = parameterName;
        this.rewriteResolver = rewriteResolver;
    }


    @Override
    public String getParameterName() {
        return parameterName;
    }

    @Override
    public Object parse(String domain) {
        if (domain == null) {
            return null;
        }
        String domainL2 = ServletUtil.getHostnameL2(domain);
        if (StringUtil.isEmpty(domainL2)) {
            return null;
        }
        return rewriteResolver.fromQueryData(null, domainL2);
    }
}
