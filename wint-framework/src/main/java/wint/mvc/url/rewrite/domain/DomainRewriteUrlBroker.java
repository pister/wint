package wint.mvc.url.rewrite.domain;

import wint.lang.utils.AutoFillArray;
import wint.mvc.url.UrlBroker;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: huangsongli
 * Date: 16/5/23
 * Time: 下午5:36
 */
public class DomainRewriteUrlBroker extends UrlBroker {

    private UrlBroker targetUrlBroker;
    private DomainRewriteHandle domainRewriteHandle;


    public DomainRewriteUrlBroker(UrlBroker targetUrlBroker, DomainRewriteHandle domainRewriteHandle) {
        this.targetUrlBroker = targetUrlBroker;
        this.domainRewriteHandle = domainRewriteHandle;
    }

    @Override
    public UrlBroker param(String name, Object value) {
        return targetUrlBroker.param(name, value);
    }

    @Override
    public UrlBroker withToken() {
        return targetUrlBroker.withToken();
    }

    @Override
    public UrlBroker parameter(String name, Object value) {
        return targetUrlBroker.parameter(name, value);
    }

    @Override
    public UrlBroker params(Object object) {
        return targetUrlBroker.params(object);
    }

    @Override
    public UrlBroker fromParams(String... names) {
        return targetUrlBroker.fromParams(names);
    }

    @Override
    public UrlBroker parameters(Object object) {
        return targetUrlBroker.parameters(object);
    }

    @Override
    public UrlBroker arg(int index, Object value) {
        return targetUrlBroker.arg(index, value);
    }

    @Override
    public UrlBroker argument(int index, Object value) {
        return targetUrlBroker.argument(index, value);
    }

    @Override
    public UrlBroker appendPath(Object inputPath) {
        return targetUrlBroker.appendPath(inputPath);
    }

    @Override
    public UrlBroker suffix(String suffix) {
        return targetUrlBroker.suffix(suffix);
    }

    @Override
    public UrlBroker arguments(Object... values) {
        return targetUrlBroker.arguments(values);
    }

    @Override
    public UrlBroker args(Object... values) {
        return targetUrlBroker.args(values);
    }

    @Override
    public String render() {
        return targetUrlBroker.render();
    }

    @Override
    public Map<String, Object> getQueryData() {
        Map<String, Object> result = new LinkedHashMap<String, Object>(targetUrlBroker.getQueryData());
        result.remove(domainRewriteHandle.getParameterName());
        return result;
    }

    @Override
    public AutoFillArray<Object> getArguments() {
        return targetUrlBroker.getArguments();
    }

    @Override
    public String getPath() {
        String path = targetUrlBroker.getPath();
        Object object = targetUrlBroker.getQueryData().get(domainRewriteHandle.getParameterName());
        String value;
        if (object == null) {
            value = domainRewriteHandle.getDefaultDomainL2Value();
        } else {
            value = domainRewriteHandle.getRewriteResolver().toQueryData(null, object);
        }
        path = path.replace(HostConstants.DOMAIN_NAME_L2, value);
        return path;
    }

    @Override
    public String getTarget() {
        return targetUrlBroker.getTarget();
    }

    @Override
    public UrlBroker setTarget(String target) {
        return targetUrlBroker.setTarget(target);
    }

    @Override
    public String getAnchor() {
        return targetUrlBroker.getAnchor();
    }

    @Override
    public UrlBroker setAnchor(String anchor) {
        return targetUrlBroker.setAnchor(anchor);
    }

    @Override
    public String getModuleName() {
        return targetUrlBroker.getModuleName();
    }

    @Override
    public String getSuffix() {
        return targetUrlBroker.getSuffix();
    }
}
