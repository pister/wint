package wint.mvc.url.rewrite;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicMap;
import wint.lang.magic.Transformer;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.url.rewrite.domain.DefaultDomainRewriteHandle;
import wint.mvc.url.rewrite.domain.DomainParser;
import wint.mvc.url.rewrite.domain.DefaultDomainParser;
import wint.mvc.url.rewrite.domain.DomainRewriteHandle;
import wint.mvc.url.rewrite.mapping.UrlRewriteMapping;
import wint.mvc.url.rewrite.mapping.UrlRewriteMappingItem;import wint.mvc.url.rewrite.resovler.RewriteResolver;

import java.util.List;
import java.util.Set;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午4:38
 */
public class DefaultUrlRewriteService extends AbstractService implements UrlRewriteService {

    private List<UrlRewriteHandle> handles = CollectionUtil.newArrayList();
    private List<UrlRewriteParser> parsers = CollectionUtil.newArrayList();

    private List<String> rewriteMappingList;

    private String domainL2Parameter;

    private String defaultDomainL2Value;

    private RewriteResolver domainL2Resolver;

    private DomainParser domainParser;

    private DomainRewriteHandle domainRewriteHandle;

    private Set<String> applyModules;

    @Override
    public void init() {
        super.init();
        MagicMap properties = serviceContext.getConfiguration().getProperties();
        final String argumentSeparater = properties.getString(Constants.PropertyKeys.URL_ARGUMENT_SEPARATER, Constants.Defaults.URL_ARGUMENT_SEPARATER);
        if (rewriteMappingList != null) {
            List<UrlRewriteMappingItem> urlRewriteMappingItems = CollectionUtil.transformList(rewriteMappingList, new Transformer<String, UrlRewriteMappingItem>() {
                @Override
                public UrlRewriteMappingItem transform(String object) {
                    return new UrlRewriteMappingItem(UrlRewriteMapping.parseFromString(object, argumentSeparater));
                }
            });
            this.setRewriteMappings(urlRewriteMappingItems);
        }
        if (StringUtil.isNotEmpty(domainL2Parameter) && domainL2Resolver != null) {
            domainParser = new DefaultDomainParser(domainL2Parameter, domainL2Resolver);
            domainRewriteHandle = new DefaultDomainRewriteHandle(domainL2Parameter, defaultDomainL2Value, domainL2Resolver);
        }
    }

    @Override
    public List<UrlRewriteHandle> getHandles() {
        return handles;
    }

    public void setHandles(List<UrlRewriteHandle> handles) {
        for (UrlRewriteHandle handle : handles) {
            this.handles.add(handle);
        }
    }

    @Override
    public DomainParser getDomainParser() {
        return domainParser;
    }

    @Override
    public DomainRewriteHandle getDomainRewriteHandle() {
        return domainRewriteHandle;
    }

    public void setRewriteMappings(List<UrlRewriteMappingItem> urlRewriteMappingItems) {
        for (UrlRewriteMappingItem urlRewriteMappingItem : urlRewriteMappingItems) {
            handles.add(urlRewriteMappingItem);
            parsers.add(urlRewriteMappingItem);
        }
    }

    @Override
    public void setRewritePatterns(List<String> rewriteMappingList) {
       this.rewriteMappingList = rewriteMappingList;
    }

    @Override
    public List<UrlRewriteParser> getParsers() {
        return parsers;
    }

    @Override
    public boolean acceptModule(String moduleName) {
        if (CollectionUtil.isEmpty(applyModules)) {
            return false;
        }
        return applyModules.contains(moduleName);
    }

    public void setParsers(List<UrlRewriteParser> parsers) {
        for (UrlRewriteParser urlRewriteParser : parsers) {
            this.parsers.add(urlRewriteParser);
        }
    }

    public void setDomainL2Parameter(String domainL2Parameter) {
        this.domainL2Parameter = StringUtil.camelToFixedString(domainL2Parameter, "-");
    }

    public void setDomainL2Resolver(RewriteResolver domainL2Resolver) {
        this.domainL2Resolver = domainL2Resolver;
    }

    public void setDefaultDomainL2Value(String defaultDomainL2Value) {
        this.defaultDomainL2Value = defaultDomainL2Value;
    }

    public void setApplyModules(List<String> applyModules) {
        this.applyModules = CollectionUtil.newHashSet(applyModules);
    }
}
