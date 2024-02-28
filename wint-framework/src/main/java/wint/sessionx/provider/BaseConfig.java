package wint.sessionx.provider;

import wint.core.config.property.PropertiesMap;
import wint.lang.magic.MagicMap;
import wint.sessionx.provider.cookie.CookieConstants;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午5:49
 */
public abstract class BaseConfig {

    private String domain;
    private int expire;
    private String path;

    public BaseConfig(PropertiesMap properties) {
        expire = properties.getInt(CookieConstants.PropertyKeys.EXPIRE, CookieConstants.DefaultValues.EXPIRE);
        domain = properties.getString(CookieConstants.PropertyKeys.DOMAIN, CookieConstants.DefaultValues.DOMAIN);
        path = properties.getString(CookieConstants.PropertyKeys.PATH, CookieConstants.DefaultValues.PATH);

    }

    public String getDomain() {
        return domain;
    }

    public int getExpire() {
        return expire;
    }

    public String getPath() {
        return path;
    }


}
