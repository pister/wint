package wint.mvc.url.rewrite.domain;

import wint.mvc.url.rewrite.resovler.RewriteResolver;

/**
 * User: huangsongli
 * Date: 16/5/23
 * Time: 下午5:30
 */
public interface DomainRewriteHandle {

    String getParameterName();

    RewriteResolver getRewriteResolver();

    String getDefaultDomainL2Value();
}
