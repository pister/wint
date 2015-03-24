package wint.mvc.url.config;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午1:21
 */
public class UrlContext {

    private String urlSuffix;

    private String argumentSeparater;

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    public String getArgumentSeparater() {
        return argumentSeparater;
    }

    public void setArgumentSeparater(String argumentSeparater) {
        this.argumentSeparater = argumentSeparater;
    }
}
