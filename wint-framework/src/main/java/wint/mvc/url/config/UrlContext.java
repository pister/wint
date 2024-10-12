package wint.mvc.url.config;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午1:21
 */
public class UrlContext {

    private String urlSuffix;

    private String argumentSeparator;

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    public String getArgumentSeparator() {
        return argumentSeparator;
    }

    public void setArgumentSeparator(String argumentSeparator) {
        this.argumentSeparator = argumentSeparator;
    }
}
