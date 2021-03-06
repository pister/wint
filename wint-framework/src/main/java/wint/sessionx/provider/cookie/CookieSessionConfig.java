package wint.sessionx.provider.cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.lang.WintException;
import wint.lang.magic.MagicMap;
import wint.lang.utils.FileUtil;
import wint.lang.utils.StreamUtil;
import wint.lang.utils.StringUtil;
import wint.sessionx.provider.BaseConfig;
import wint.sessionx.util.WebResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pister on 14-2-26.
 */
public class CookieSessionConfig extends BaseConfig {

    private static final Logger log = LoggerFactory.getLogger(CookieSessionConfig.class);

    private boolean encrypt;

    private String encryptKey;

    private String dataSeparate;

    private int cookieDataMaxSize;

    private String prefixName;

    private int cookieDataIndex;

    public CookieSessionConfig(MagicMap properties) {
        super(properties);
        encrypt = properties.getBoolean(CookieConstants.PropertyKeys.ENCRYPT, CookieConstants.DefaultValues.ENCRYPT);
        encryptKey = properties.getString(CookieConstants.PropertyKeys.ENCRYPT_KEY, CookieConstants.DefaultValues.ENCRYPT_KEY);

        if (!properties.containsKey(CookieConstants.PropertyKeys.ENCRYPT_KEY) && properties.containsKey(CookieConstants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH)) {
            String sessionCookieKeyPath = properties.getString(CookieConstants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH, CookieConstants.DefaultValues.COOKIE_ENCRYPTKEY_PATH);
            encryptKey = loadCookieSessionKey(sessionCookieKeyPath);
        }
        encryptKey = StringUtil.trimToEmpty(encryptKey);

        dataSeparate = properties.getString(CookieConstants.PropertyKeys.DATA_SEPARATE, CookieConstants.DefaultValues.DATA_SEPARATE);
        cookieDataMaxSize = properties.getInt(CookieConstants.PropertyKeys.DATA_MAX_SIZE, CookieConstants.DefaultValues.DATA_MAX_SIZE);
        prefixName = properties.getString(CookieConstants.PropertyKeys.COOKIE_DATA_PREFIX, CookieConstants.DefaultValues.COOKIE_DATA_PREFIX);
        cookieDataIndex = properties.getInt(CookieConstants.PropertyKeys.COOKIE_DATA_INDEX, CookieConstants.DefaultValues.COOKIE_DATA_INDEX);
    }


    private String loadCookieSessionKey(String sessionCookieKeyPath) {
        try {
            InputStream is = getSessionCookieKeyInputStream(sessionCookieKeyPath);
            if (is == null) {
                log.warn("no wint-session-key file found, use default cookie session key!!!");
                return CookieConstants.DefaultValues.ENCRYPT_KEY;
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
        is = StreamUtil.getUserHomeResourceAsStream(CookieConstants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH);
        if (is != null) {
            log.warn("session key load from resource: " + new File(new File(System.getProperty("user.home")), CookieConstants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH));
            return is;
        }
        is = WebResourceUtil.getWebResouceAsStream(CookieConstants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH);
        if (is != null) {
            log.warn("session key load from resource: " + CookieConstants.PropertyKeys.COOKIE_ENCRYPTKEY_PATH);
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

    public int getCookieDataIndex() {
        return cookieDataIndex;
    }
}
