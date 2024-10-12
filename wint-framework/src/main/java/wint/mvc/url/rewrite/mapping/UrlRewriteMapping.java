package wint.mvc.url.rewrite.mapping;

import wint.lang.magic.Transformer;
import wint.lang.utils.*;
import wint.mvc.parameters.MapParameters;
import wint.mvc.url.UrlBroker;
import wint.mvc.url.UrlBrokerUtil;
import wint.mvc.url.config.UrlContext;
import wint.mvc.url.rewrite.RequestData;
import wint.mvc.url.rewrite.resovler.DefaultRewriteResolver;
import wint.mvc.url.rewrite.resovler.RewriteResolver;
import wint.mvc.url.rewrite.domain.HostConstants;

import java.util.*;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午7:39
 */
public class UrlRewriteMapping {

    private static final String ESCAPE_FLAG = "!";

    private static final String PARAM_SEPARATOR = "/";

    private static final RewriteResolver defaultRewriteResolver = new DefaultRewriteResolver();

    private String baseTarget;
    private List<Item> patternItems;
    private List<String> baseTargetParts;

    public UrlRewriteMapping(String baseTarget, List<Item> patternItems) {
        this.baseTarget = normalizeTarget(baseTarget);
        this.baseTargetParts = StringUtil.splitTrim(this.baseTarget, PARAM_SEPARATOR);
        this.patternItems = patternItems;
    }

    private static String normalizeTarget(String target) {
        String theTarget = UrlBrokerUtil.normalizePath(target);
        return StringUtil.camelToFixedString(theTarget, "-");
    }

    /**
     * /aa/bb:cc-dd-ee-ff-gg.htm
     * =>
     * target: aa/bb
     * queryStringName cc,dd,ee,ff,gg
     *
     * @param pattern
     */
    public static UrlRewriteMapping parseFromString(String pattern) {
        String target = StringUtil.getFirstBefore(pattern, ":");
        String itemName = StringUtil.getFirstAfter(pattern, ":");
        String[] names = itemName.split(PARAM_SEPARATOR);
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
        if (inputTarget.startsWith("/")) {
            inputTarget = inputTarget.substring(1);
        }
        String newTarget = normalizeTarget(inputTarget);
        return newTarget.startsWith(baseTarget);
    }

    public boolean matches(UrlBroker urlBroker) {
        StringBuilder sb = new StringBuilder();
        sb.append(urlBroker.getPath());
        String target = UrlBrokerUtil.trimLastSlash(normalizeTarget(urlBroker.getTarget()));
        sb.append(target);
        String fullPath = sb.toString();
        // get after from protocol
        String urlTarget = StringUtil.getFirstAfter(fullPath, "//");
        // get after from host
        urlTarget = StringUtil.getFirstAfter(urlTarget, "/");
        urlTarget = normalizeTarget(urlTarget);
        return urlTarget.startsWith(baseTarget);

    }

    private int getParamStartIndex(List<String> targetParts) {
        int i = 0;
        for (int len = Math.min(baseTargetParts.size(), targetParts.size()); i < len; i++) {
            String basePart = baseTargetParts.get(i);
            String inputPart = StringUtil.camelToFixedString(targetParts.get(i), "-");
            if (!StringUtil.equals(basePart, inputPart)) {
                return i;
            }
        }
        return i;
    }

    public RequestData parse(String target, UrlContext urlContext) {
        final String suffix = urlContext.getUrlSuffix();
        target = trimFromSuffix(target, suffix);

        final List<String> targetParts = StringUtil.splitTrim(target, PARAM_SEPARATOR);
        final int partLength = targetParts.size();
        final int paramIndexStart = getParamStartIndex(targetParts);
        int paramIndex = paramIndexStart;
        Map<String, String[]> mapParameters = MapUtil.newHashMap();

        for (Item item : patternItems) {
            if (paramIndex >= partLength) {
                break;
            }
            String value = targetParts.get(paramIndex);
            mapParameters.put(item.getName(), new String[]{value});
            paramIndex++;
        }

        RequestData requestData = new RequestData();
        requestData.setParameters(new MapParameters(mapParameters));
        requestData.setTarget(CollectionUtil.join(targetParts.subList(0, paramIndexStart), PARAM_SEPARATOR));
        return requestData;
    }

    private static String trimSeparators(String rewritePath, String separator) {
        if (StringUtil.isEmpty(separator)) {
            return rewritePath;
        }
        final int separatorLen = separator.length();
        while (rewritePath.endsWith(separator)) {
            rewritePath = rewritePath.substring(0, rewritePath.length() - separatorLen);
        }
        return rewritePath;
    }

    public String rewrite(UrlBroker urlBroker, String urlSuffix) {
        final Map<String, Object> queryData = new LinkedHashMap<String, Object>(urlBroker.getQueryData());

        Item domainL2Item = null;
        List<Item> writeItems = CollectionUtil.newArrayList(patternItems.size());
        for (Item item : patternItems) {
            if (item.getName().endsWith(HostConstants.DOMAIN_NAME_L2)) {
                domainL2Item = item;
            } else {
                writeItems.add(item);
            }
        }

        StringBuilder sb = new StringBuilder();
        String path = urlBroker.getPath();
        sb.append(path);
        String target = UrlBrokerUtil.trimLastSlash(normalizeTarget(urlBroker.getTarget()));
        sb.append(target);
        final Set<String> rewritedNames = new HashSet<String>();
        String rewritePath = CollectionUtil.join(writeItems, PARAM_SEPARATOR, new Transformer<Item, String>() {
            @Override
            public String transform(Item item) {
                String name = item.getName();
                name = StringUtil.camelToFixedString(name, "-");
                Object value = queryData.get(name);
                rewritedNames.add(name);
                if (value == null) {
                    return StringUtil.EMPTY;
                }
                String stringValue = defaultRewriteResolver.toQueryData(item, value);
                return stringValue;
            }
        }, null);

        rewritePath = trimSeparators(rewritePath, PARAM_SEPARATOR);
        if (!StringUtil.isEmpty(rewritePath)) {
            if (sb.charAt(sb.length() - 1) != '/') {
                sb.append("/");
            }
            sb.append(rewritePath);
        }

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

    public String getBaseTarget() {
        return baseTarget;
    }

    public List<Item> getPatternItems() {
        return patternItems;
    }
}
