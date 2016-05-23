package wint.mvc.url.rewrite;

import wint.core.service.Service;
import wint.mvc.url.rewrite.domain.DomainParser;
import wint.mvc.url.rewrite.domain.DomainRewriteHandle;

import java.util.List;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午4:38
 */
public interface UrlRewriteService extends Service {

    List<UrlRewriteHandle> getHandles();

    List<UrlRewriteParser> getParsers();

    DomainParser getDomainParser();

    DomainRewriteHandle getDomainRewriteHandle();

    void setRewritePatterns(List<String> rewriteMappingList);
}
