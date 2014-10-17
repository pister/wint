package wint.sessionx.provider;

import wint.lang.magic.MagicMap;
import wint.sessionx.provider.cookie.CookieContants;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午5:49
 */
public abstract class BaseConfig {

    private String domain;
    private int expire;
    private String path;

    public BaseConfig(MagicMap properties) {
        expire = properties.getInt(CookieContants.PropertyKeys.EXPIRE, CookieContants.DefaultValues.EXPIRE);
        domain = properties.getString(CookieContants.PropertyKeys.DOMAIN, CookieContants.DefaultValues.DOMAIN);
        path = properties.getString(CookieContants.PropertyKeys.PATH, CookieContants.DefaultValues.PATH);

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
