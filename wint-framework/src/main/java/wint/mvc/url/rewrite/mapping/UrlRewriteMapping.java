package wint.mvc.url.rewrite.mapping;

import wint.lang.magic.Transformer;
import wint.lang.utils.*;
import wint.mvc.parameters.MapParameters;
import wint.mvc.url.UrlBroker;
import wint.mvc.url.UrlBrokerUtil;
import wint.mvc.url.config.UrlContext;
import wint.mvc.url.rewrite.RequestData;
import wint.mvc.url.rewrite.UrlBase64;

import java.util.*;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午7:39
 */
public class UrlRewriteMapping {

    private static final String ESCAPE_FLAG = "!";

    private static final String CHARSET = "utf-8";

    private static Transformer<Object, String> transformer;

    static {
        transformer = new Transformer<Object, String>() {
            public String transform(Object object) {
                if (object == null) {
                    return StringUtil.EMPTY;
                }
                if (object instanceof String) {
                    String stringValue = (String)object;
                    stringValue = UrlUtil.encode(stringValue, CHARSET);
                    return stringValue;
                } if (object instanceof Date) {
                    String stringValue = DateUtil.formatDate(object, "yyyyMMddHHmmss");
                    return stringValue;
                } else if (object.getClass().isArray()) {
                    Object[] array = (Object[])object;
                    return ArrayUtil.join(array, ",");
                } else if (object instanceof Collection) {
                    Collection<?> c = (Collection<?>)object;
                    return CollectionUtil.join(c, ",");
                } else {
                    return object.toString();
                }
            }
        };
    }


    private String baseTarget;
    private List<Item> patternItems;



    public UrlRewriteMapping(String baseTarget) {
        this.baseTarget = UrlBrokerUtil.normalizePath(baseTarget);
        this.patternItems =  CollectionUtil.newArrayList();
    }

    public UrlRewriteMapping(String baseTarget, List<Item> patternItems) {
        this.baseTarget = UrlBrokerUtil.normalizePath(baseTarget);
        this.patternItems = patternItems;
    }

    /**
     * /aa/bb/cc-dd-ee-ff-gg.htm
     * =>
     * target: aa/bb
     * queryStringName cc,dd,ee,ff,gg
     * @param pattern
     */
    public static UrlRewriteMapping parseFromString(String pattern, String separater) {
        String target = StringUtil.getLastBefore(pattern, "/");
        String itemName = StringUtil.getLastAfter(pattern, "/");
        String[] names = itemName.split(separater);
        List<Item> patternItems = CollectionUtil.newArrayList();
        for (String name : names) {
            if (name.startsWith(ESCAPE_FLAG)) {
                name = StringUtil.getFirstAfter(name, ESCAPE_FLAG);
                patternItems.add(new Item(name, true));
            } else {
                patternItems.add(new Item(name, false));
            }
        }
        return new UrlRewriteMapping(target, patternItems);
    }

    public void addPatternItem(Item item) {
        patternItems.add(item);
    }

    private String trimFromSuffix(String target, String suffix) {
        if (target.endsWith(suffix)) {
            target = target.substring(0, target.length() - suffix.length());
        }
        return target;
    }

    public boolean matches(String inputTarget) {
        inputTarget = StringUtil.getLastBefore(inputTarget, ".");
        String newTarget = StringUtil.getLastBefore(inputTarget, "/");
        newTarget = UrlBrokerUtil.normalizePath(newTarget);
        return StringUtil.equals(baseTarget, newTarget);
    }

    public boolean matches(UrlBroker urlBroker) {
        StringBuilder sb = new StringBuilder();
        sb.append(urlBroker.getPath());
        String target = UrlBrokerUtil.trimLastSlash(UrlBrokerUtil.normalizePath(urlBroker.getTarget()));
        sb.append(target);
        String fullPath = sb.toString();
        // get after from protocol
        String urlTarget = StringUtil.getFirstAfter(fullPath, "//");
        // get after from host
        urlTarget = StringUtil.getFirstAfter(urlTarget, "/");
        urlTarget = UrlBrokerUtil.normalizePath(urlTarget);
        return StringUtil.equals(this.baseTarget, urlTarget);
    }

    public RequestData parse(String target, UrlContext urlContext) {
        final String suffix = urlContext.getUrlSuffix();
        target = trimFromSuffix(target, suffix);
        String path = StringUtil.getLastAfter(target, "/");
        String newTarget = StringUtil.getLastBefore(target, "/");
        newTarget = UrlBrokerUtil.normalizePath(newTarget);
        String[] parts = path.split(urlContext.getArgumentSeparater());
        int i = 0;
        int partsLength = parts.length;
        Map<String, String[]> mapParameters = MapUtil.newHashMap();
        for (Item item : patternItems) {
            if (i >= partsLength) {
                break;
            }
            String part = parts[i];
            if (item.isBase64()) {
                try {
                    part = UrlBase64.decodeBase64(part);
                } catch (Exception e) {
                    part = null;
                }
            }
            mapParameters.put(item.getName(), new String[]{part});
            i++;
        }
        RequestData requestData = new RequestData();
        requestData.setParameters(new MapParameters(mapParameters));
        requestData.setTarget(newTarget);
        return requestData;
    }

    private static String trimSeparaters(String rewritePath, String separater) {
        if (StringUtil.isEmpty(separater)) {
            return rewritePath;
        }
        final int separaterLen = separater.length();
        while (rewritePath.endsWith(separater)) {
            rewritePath = rewritePath.substring(0, rewritePath.length() - separaterLen);
        }
        return rewritePath;
    }

    public String rewrite(UrlBroker urlBroker, UrlContext urlContext) {
        final Map<String, Object> queryData = new LinkedHashMap<String, Object>(urlBroker.getQueryData());
        StringBuilder sb = new StringBuilder();
        sb.append(urlBroker.getPath());
        String target = UrlBrokerUtil.trimLastSlash(UrlBrokerUtil.normalizePath(urlBroker.getTarget()));
        sb.append(target);
        final Set<String> rewritedNames = new HashSet<String>();
        String rewritePath = CollectionUtil.join(patternItems, urlContext.getArgumentSeparater(), new Transformer<Item, String>() {
            @Override
            public String transform(Item item) {
                Object value = queryData.get(item.getName());
                rewritedNames.add(item.getName());
                if (value == null) {
                    return StringUtil.EMPTY;
                }
                String stringValue;
                if (item.isBase64()) {
                    stringValue = UrlBase64.encodeBase64(value.toString());
                } else {
                    stringValue = transformer.transform(value);
                }
                return stringValue;
            }
        });

        rewritePath = trimSeparaters(rewritePath, urlContext.getArgumentSeparater());
        if (!StringUtil.isEmpty(rewritePath)) {
            sb.append("/");
            sb.append(rewritePath);
        }

        String urlSuffix = urlContext.getUrlSuffix();
        if (!StringUtil.isEmpty(urlBroker.getSuffix())) {
            urlSuffix = urlBroker.getSuffix();
        }
        if (!StringUtil.isEmpty(urlSuffix) && !StringUtil.isEmpty(target)) {
            sb.append(urlSuffix);
        }

        for (String name : rewritedNames) {
            queryData.remove(name);
        }

        if (!queryData.isEmpty()) {
            String queryString = MapUtil.join(queryData, "=", "&", UrlBrokerUtil.getTransformer());
            sb.append("?");
            sb.append(queryString);
        }

        return sb.toString();
    }


}
