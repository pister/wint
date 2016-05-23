package wint.mvc.url.rewrite.domain;

import wint.mvc.url.rewrite.resovler.RewriteResolver;

/**
 * User: huangsongli
 * Date: 16/5/23
 * Time: 下午5:33
 */
public class DefaultDomainRewriteHandle implements DomainRewriteHandle {

    private String parameterName;

    private String defaultDomainL2Value;

    private RewriteResolver rewriteResolver;

    public DefaultDomainRewriteHandle(String parameterName, String defaultDomainL2Value, RewriteResolver rewriteResolver) {
        this.parameterName = parameterName;
        this.defaultDomainL2Value = defaultDomainL2Value;
        this.rewriteResolver = rewriteResolver;
    }


    @Override
    public String getParameterName() {
        return parameterName;
    }

    @Override
    public RewriteResolver getRewriteResolver() {
        return rewriteResolver;
    }

    @Override
    public String getDefaultDomainL2Value() {
        return defaultDomainL2Value;
    }
}
