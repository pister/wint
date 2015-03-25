package wint.mvc.url;

import wint.core.config.Constants;
import wint.lang.convert.ConvertUtil;
import wint.lang.magic.MagicMap;
import wint.lang.magic.Transformer;
import wint.lang.utils.*;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author pister 2012-3-2 09:23:27
 */
public class UrlBrokerUtil {


    private static final String HTTPS = "https://";

    private static final String HTTP = "http://";

    private static final String  CHARSET = Constants.Defaults.CHARSET_ENCODING;

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
                    String stringValue = DateUtil.formatDate(object,  "yyyyMMddHHmmss");
                    stringValue = UrlUtil.encode(stringValue, CHARSET);
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

    public static Transformer<Object, String> getTransformer() {
        return transformer;
    }

    public static Tuple<String /*path*/, String /*target*/> parseTarget(String path, String target, String pathAsTargetName) {
        if (pathAsTargetName.equals(target)) {
            int lastSlashPos = path.lastIndexOf("/");
            if (lastSlashPos < 0) {
                // 没有/的情况
                return new Tuple<String, String>(path, StringUtil.EMPTY);
            }
            if (path.startsWith(HTTP) && lastSlashPos < HTTP.length()) {
                return new Tuple<String, String>(path, StringUtil.EMPTY);
            }
            if (path.startsWith(HTTPS) && lastSlashPos < HTTPS.length()) {
                return new Tuple<String, String>(path, StringUtil.EMPTY);
            }
            int firstSlash = target.indexOf("/");
            if (lastSlashPos == firstSlash + 1) {
                // http://hostname
                // https://hostname
                return new Tuple<String, String>(path, StringUtil.EMPTY);
            }
            String targetPath = path.substring(0, lastSlashPos);
            String newTarget = path.substring(lastSlashPos + 1);
            // http://hostname/abc/efg
            // => http://hostname/abc and efg
            return new Tuple<String, String>(targetPath, newTarget);

        } else {
            return new Tuple<String, String>(path, target);
        }
    }


    public static String appendPath(String basePath, Object inputPath) {
        StringBuilder sb = new StringBuilder(basePath);
        if (!basePath.endsWith("/")) {
            sb.append("/");
        }
        sb.append(UrlBrokerUtil.normalizePath(ConvertUtil.toString(inputPath)));
        return sb.toString();
    }

    /**
     * null => ""
     * ""	=> ""
     * "/"	=> ""
     * "abc"	=> "abc/"
     * "abc/"	=> "abc/"
     * "/abc/"	=> "abc/"
     * "\abc\"	=> "abc/"
     * "//abc/"	=> "abc/"
     * "aaa/ccc"	=> "abc/ccc/"
     *
     * @param path
     * @return
     */
    public static String normalizePath(String path) {
        if (StringUtil.isEmpty(path)) {
            return StringUtil.EMPTY;
        }
        path = path.replace('\\', '/');
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (StringUtil.isEmpty(path)) {
            return StringUtil.EMPTY;
        }
        if (path.endsWith("/")) {
            return path;
        }
        return path + "/";
    }

    public static String trimLastSlash(String url) {
        String path = url;
        if (StringUtil.isEmpty(path)) {
            return StringUtil.EMPTY;
        }
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public static String renderUrlBroker(UrlBroker urlBroker, String urlSuffix, String argumentSeparater, Transformer<Object, String> transformer) {
        StringBuilder sb = new StringBuilder();
        sb.append(urlBroker.getPath());
        String target = trimLastSlash(normalizePath(urlBroker.getTarget()));
        sb.append(target);
        AutoFillArray<Object> arguments = urlBroker.getArguments();
        if (!arguments.isEmpty()) {
            sb.append("/");
            String argumentsString = arguments.join("", argumentSeparater);
            sb.append(argumentsString);
        }
        if (!StringUtil.isEmpty(urlBroker.getSuffix())) {
            urlSuffix = urlBroker.getSuffix();
        }
        if (!StringUtil.isEmpty(urlSuffix) && !StringUtil.isEmpty(target)) {
            sb.append(urlSuffix);
        }
        Map<String, Object> queryData = urlBroker.getQueryData();
        if (!queryData.isEmpty()) {
            String queryString = MapUtil.join(queryData, "=", "&", transformer);
            sb.append("?");
            sb.append(queryString);
        }
        String anchor = urlBroker.getAnchor();
        if (!StringUtil.isEmpty(anchor)) {
            sb.append("#");
            sb.append(anchor);
        }
        return sb.toString();
    }

}
