package wint.mvc.url.rewrite;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.lang.magic.MagicMap;
import wint.lang.magic.Transformer;
import wint.lang.utils.CollectionUtil;
import wint.mvc.url.rewrite.mapping.UrlRewriteMapping;
import wint.mvc.url.rewrite.mapping.UrlRewriteMappingItem;

import java.util.List;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午4:38
 */
public class DefaultUrlRewriteService extends AbstractService implements UrlRewriteService {

    private List<UrlRewriteHandle> handles = CollectionUtil.newArrayList();
    private List<UrlRewriteParser> parsers = CollectionUtil.newArrayList();

    private List<String> rewriteMappingList;

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

    public void setParsers(List<UrlRewriteParser> parsers) {
        for (UrlRewriteParser urlRewriteParser : parsers) {
            this.parsers.add(urlRewriteParser);
        }
    }

}
