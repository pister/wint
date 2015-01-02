package wint.mvc.url;

import wint.core.config.Constants;
import wint.help.mvc.security.csrf.CsrfTokenUtil;
import wint.lang.convert.ConvertUtil;
import wint.lang.exceptions.UrlException;
import wint.lang.magic.MagicMap;
import wint.lang.utils.AutoFillArray;
import wint.lang.utils.NumberUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.Tuple;
import wint.mvc.view.Render;

/**
 * 一个urlBroker类，用于生成URL
 *
 * @author pister 2012-3-2 07:50:40
 */
public class UrlBroker implements Render {

    private UrlBrokerService urlBrokerService;
    private String path;
    private String target;
    private MagicMap queryData = MagicMap.newMagicMap();
    private AutoFillArray<Object> arguments = new AutoFillArray<Object>();
    private String anchor;
    private String tokenName;
    private String pathAsTargetName;
    private boolean pathHasNumber;
    private String suffix;

    public UrlBroker(UrlBrokerService urlBrokerService, String path, String target, String tokenName, String pathAsTargetName, boolean pathHasNumber) {
        super();
        this.urlBrokerService = urlBrokerService;
        this.path = UrlBrokerUtil.normalizePath(path);
        target = StringUtil.getLastBefore(target, ".");
        this.target = StringUtil.camelToFixedString(target, "-");
        this.tokenName = tokenName;
        this.pathAsTargetName = pathAsTargetName;
        this.pathHasNumber = pathHasNumber;
    }

    /**
     * 参数，用于生成queryString
     * 同parameter
     *
     * @param name
     * @param value
     * @return
     */
    public UrlBroker param(String name, Object value) {
        return parameter(name, value);
    }

    /**
     * url上附带csrf token
     *
     * @return
     */
    public UrlBroker withToken() {
        return parameter(tokenName, CsrfTokenUtil.token());
    }

    /**
     * 参数，用于生成queryString
     *
     * @param name
     * @param value
     * @return
     */
    public UrlBroker parameter(String name, Object value) {
        name = StringUtil.camelToFixedString(name, "-");
        queryData.put(name, value);
        return this;
    }

    /**
     * 设置arg参数
     * 同argument
     *
     * @param index
     * @param value
     * @return
     */
    public UrlBroker arg(int index, Object value) {
        return argument(index, value);
    }

    /**
     * 设置arg参数
     *
     * @param index
     * @param value
     * @return
     */
    public UrlBroker argument(int index, Object value) {
        if (StringUtil.isEmpty(target)) {
            this.setTarget(pathAsTargetName);
        }
        if (index > Constants.Url.MAX_ARGUMENT_COUNT) {
            throw new UrlException("url argument index is too large: " + index);
        }
        if (index == 0) {
            if (!pathHasNumber && !NumberUtil.isPositiveInteger(value)) {
                throw new UrlException("the first argument for url[" + target + "] must be a Positive Integer.");
            }
        }
        arguments.set(index, ArgumentEncoder.transformStringArgument(value));
        return this;
    }

    public UrlBroker appendPath(Object inputPath) {
        if (NumberUtil.isNumeric(ConvertUtil.toString(inputPath))) {
            pathHasNumber = true;
        }
        this.path = UrlBrokerUtil.appendPath(this.path, inputPath);
        return this;
    }

    public UrlBroker suffix(String suffix) {
        if (suffix != null && !suffix.startsWith(".")) {
            suffix = "." + suffix;
        }
        this.suffix = suffix;
        return this;
    }

    /**
     * 一次设置多个arg参数，参数索引从0开始
     *
     * @param values
     * @return
     */
    public UrlBroker arguments(Object... values) {
        if (values == null) {
            return this;
        }
        int index = 0;
        for (Object object : values) {
            argument(index, object);
            ++index;
        }
        return this;
    }

    /**
     * 一次设置多个arg参数，参数索引从0开始
     * 同arguments
     *
     * @param values
     * @return
     */
    public UrlBroker args(Object... values) {
        return arguments(values);
    }

    public String render() {
        if (StringUtil.isEmpty(target)) {
            this.setTarget(pathAsTargetName);
        }
        return urlBrokerService.render(this);
    }

    public MagicMap getQueryData() {
        return queryData;
    }

    public AutoFillArray<Object> getArguments() {
        return arguments;
    }

    public String getPath() {
        return path;
    }

    public String getTarget() {
        return target;
    }

    public UrlBroker setTarget(String target) {
        String basePath = path;
        while (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        Tuple<String, String> pathAndTarget = UrlBrokerUtil.parseTarget(basePath, target, pathAsTargetName);

        this.path = UrlBrokerUtil.normalizePath(pathAndTarget.getT1());
        this.target = pathAndTarget.getT2();
        return this;
    }

    public String getAnchor() {
        return anchor;
    }

    public UrlBroker setAnchor(String anchor) {
        this.anchor = anchor;
        return this;
    }

    public String getSuffix() {
        return suffix;
    }
}
