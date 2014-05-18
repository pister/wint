package wint.sessionx.provider.cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.lang.WintException;
import wint.lang.magic.MagicMap;
import wint.lang.utils.FileUtil;
import wint.lang.utils.StreamUtil;
import wint.lang.utils.StringUtil;
import wint.sessionx.util.WebResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pister on 14-2-26.
 */
public class CookieSessionConfig {

    private static final Logger log = LoggerFactory.getLogger(CookieSessionConfig.class);

    private boolean encrypt;

    private String encryptKey;

    private String dataSeparate;

    private int cookieDataMaxSize;

    private String prefixName;

    private int cookieDataIndex;

    private String domain;

    private int expire;

    private String path;

    public CookieSessionConfig(MagicMap properties) {
        encrypt = properties.getBoolean(CookieContants.PropertyKeys.ENCRYPT, CookieContants.DefaultValues.ENCRYPT);
        encryptKey = properties.getString(CookieContants.PropertyKeys.ENCRYPT_KEY, CookieContants.DefaultValues.ENCRYPT_KEY);

        if (!properties.containsKey(CookieContants.PropertyKeys.ENCRYPT_KEY) && properties.containsKey(CookieContants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH)) {
            String sessionCookieKeyPath = properties.getString(CookieContants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH, CookieContants.DefaultValues.COOKIE_ENCRYPTKEY_PATH);
            encryptKey = loadCookieSessionKey(sessionCookieKeyPath);
        }
        encryptKey = StringUtil.trimToEmpty(encryptKey);

        dataSeparate = properties.getString(CookieContants.PropertyKeys.DATA_SEPARATE, CookieContants.DefaultValues.DATA_SEPARATE);
        cookieDataMaxSize = properties.getInt(CookieContants.PropertyKeys.DATA_MAX_SIZE, CookieContants.DefaultValues.DATA_MAX_SIZE);
        expire = properties.getInt(CookieContants.PropertyKeys.EXPIRE, CookieContants.DefaultValues.EXPIRE);
        prefixName = properties.getString(CookieContants.PropertyKeys.COOKIE_DATA_PREFIX, CookieContants.DefaultValues.COOKIE_DATA_PREFIX);
        cookieDataIndex = properties.getInt(CookieContants.PropertyKeys.COOKIE_DATA_INDEX, CookieContants.DefaultValues.COOKIE_DATA_INDEX);
        domain = properties.getString(CookieContants.PropertyKeys.DOMAIN, CookieContants.DefaultValues.DOMAIN);
        path = properties.getString(CookieContants.PropertyKeys.PATH, CookieContants.DefaultValues.PATH);

    }


    private String loadCookieSessionKey(String sessionCookieKeyPath) {
        try {
            InputStream is = getSessionCookieKeyInputStream(sessionCookieKeyPath);
            if (is == null) {
                log.warn("no wint-session-key file found, use default cookie session key!!!");
                return CookieContants.DefaultValues.ENCRYPT_KEY;
            } else {
                return StreamUtil.readAsString(is);
            }
        } catch (IOException e) {
            throw new WintException(e);
        }
    }

    private InputStream getSessionCookieKeyInputStream(String sessionCookieKeyPath) throws IOException {
        if (FileUtil.existFile(sessionCookieKeyPath)) {
            log.warn("session key load from: " + sessionCookieKeyPath);
            return new FileInputStream(sessionCookieKeyPath);
        }
        InputStream is = StreamUtil.getUserHomeResourceAsStream(sessionCookieKeyPath);
        if (is != null) {
            log.warn("session key load from: " + new File(new File(System.getProperty("user.home")), sessionCookieKeyPath));
            return is;
        }
        is = StreamUtil.getUserHomeResourceAsStream(CookieContants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH);
        if (is != null) {
            log.warn("session key load from resource: " + new File(new File(System.getProperty("user.home")), CookieContants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH));
            return is;
        }
        is = WebResourceUtil.getWebResouceAsStream(CookieContants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH);
        if (is != null) {
            log.warn("session key load from resource: " + CookieContants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH);
            return is;
        }
        return is;
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

    public int getCookieDataIndex() {
        return cookieDataIndex;
    }
}
