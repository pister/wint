package wint.sessionx.provider.cookie;

import wint.lang.magic.MagicMap;

/**
 * Created by pister on 14-2-26.
 */
public class CookieSessionConfig {

    private boolean encrypt;

    private String encryptKey;

    private String dataSeparate;

    private int cookieDataMaxSize;

    private String prefixName;

    private String domain;

    private int expire;

    private String path;

    public CookieSessionConfig(MagicMap properties) {
        encrypt = properties.getBoolean(CookieContants.PropertyKeys.ENCRYPT, CookieContants.DefaultValues.ENCRYPT);
        encryptKey = properties.getString(CookieContants.PropertyKeys.ENCRYPT_KEY, CookieContants.DefaultValues.ENCRYPT_KEY);
        dataSeparate = properties.getString(CookieContants.PropertyKeys.DATA_SEPARATE, CookieContants.DefaultValues.DATA_SEPARATE);
        cookieDataMaxSize = properties.getInt(CookieContants.PropertyKeys.DATA_MAX_SIZE, CookieContants.DefaultValues.DATA_MAX_SIZE);
        expire = properties.getInt(CookieContants.PropertyKeys.EXPIRE, CookieContants.DefaultValues.EXPIRE);
        prefixName = properties.getString(CookieContants.PropertyKeys.COOKIE_DATA_PREFIX, CookieContants.DefaultValues.COOKIE_DATA_PREFIX);
        domain = properties.getString(CookieContants.PropertyKeys.DOMAIN, CookieContants.DefaultValues.DOMAIN);
        path = properties.getString(CookieContants.PropertyKeys.PATH, CookieContants.DefaultValues.PATH);
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public String getDataSeparate() {
        return dataSeparate;
    }

    public int getCookieDataMaxSize() {
        return cookieDataMaxSize;
    }

    public String getPrefixName() {
        return prefixName;
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
