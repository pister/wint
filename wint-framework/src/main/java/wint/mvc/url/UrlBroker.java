package wint.mvc.url;

import wint.core.config.Constants;
import wint.help.mvc.security.csrf.CsrfTokenUtil;
import wint.lang.convert.ConvertUtil;
import wint.lang.exceptions.UrlException;
import wint.lang.magic.MagicObject;
import wint.lang.magic.Property;
import wint.lang.utils.AutoFillArray;
import wint.lang.utils.NumberUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.Tuple;
import wint.mvc.flow.FlowData;
import wint.mvc.holder.WintContext;
import wint.mvc.parameters.Parameters;
import wint.mvc.view.Render;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 一个urlBroker类，用于生成URL
 *
 * @author pister 2012-3-2 07:50:40
 */
public class UrlBroker implements Render {

    private String moduleName;
    private UrlBrokerService urlBrokerService;
    private String path;
    private String target;
    private LinkedHashMap<String, Object> queryData = new LinkedHashMap<String, Object>();
    private AutoFillArray<Object> arguments = new AutoFillArray<Object>();
    private String anchor;
    private String tokenName;
    private String pathAsTargetName;
    private boolean pathHasNumber;
    private String suffix;

    protected UrlBroker() {}

    public UrlBroker(String moduleName, UrlBrokerService urlBrokerService, String path, String target,
                     String tokenName, String pathAsTargetName, boolean pathHasNumber) {
        super();
        this.moduleName = moduleName;
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
     * 把对象的属性作为参数传入
     * @param object
     * @since 1.3.9.8
     * @return
     */
    public UrlBroker params(Object object) {
        return parameters(object);
    }

    public UrlBroker removeParam(String name) {
        name = StringUtil.camelToFixedString(name, "-");
        queryData.remove(name);
        return this;
    }

    /**
     * 从url的params中复制
     * @param names
     * @return
     */
    public UrlBroker fromParams(String ...names) {
        if (names == null || names.length == 0) {
            return this;
        }
        FlowData flowData = WintContext.getFlowData();
        if (flowData == null) {
            return this;
        }
        Parameters parameters = flowData.getParameters();
        for (String name : names) {
            String[] values = parameters.getStringArray(name);
            if (values == null) {
                continue;
            }
            for (String value : values) {
                parameter(name, value);
            }
        }
        return this;
    }

    /**
     * 把对象的属性作为参数传入
     * @param object
     * @since 1.3.9.8
     * @return
     */
    public UrlBroker parameters(Object object) {
        if (object == null) {
            return this;
        }
        if (object instanceof Map) {
            Map<String, Object> mapItems = (Map<String, Object>)object;
            for (Map.Entry<String, Object> entry : mapItems.entrySet()) {
                parameter(String.valueOf(entry.getKey()), entry.getValue());
            }
        } else {
            MagicObject mo = MagicObject.wrap(object);
            Map<String, Property> propertyMap = mo.getMagicClass().getReadAndWritableProperties();
            for (Map.Entry<String, Property> propertyEntry : propertyMap.entrySet()) {
                String name = propertyEntry.getKey();
                Property property = propertyEntry.getValue();
                Object value = property.getValue(object);
                parameter(name, value);
            }
        }
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

    public Map<String, Object> getQueryData() {
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

    public String getModuleName() {
        return moduleName;
    }

    public String getSuffix() {
        return suffix;
    }
}
