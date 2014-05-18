package wint.sessionx.provider.cookie;

import wint.core.config.Constants;

/**
 * Created by pister on 14-2-26.
 */
public interface CookieContants {

    String URL_ENCODE_CHARSET = Constants.Defaults.CHARSET_ENCODING;

    String WINT_COOKIE_MAGIC_TOKEN = "wintx:";

    interface PropertyKeys {
        String ENCRYPT = "wint.sessionx.cookie.encrypt";
        String ENCRYPT_KEY = "wint.session.cookie.encrypt.key";
        String DATA_SEPARATE = "wint.session.cookie.data.separate";
        String DATA_MAX_SIZE = "wint.session.cookie.data.maxsize";
        String COOKIE_DATA_PREFIX = "wint.session.cookie.data.prefix";
        String COOKIE_DATA_INDEX = "wint.session.cookie.data.index";
        String DOMAIN = "wint.session.cookie.domain";
        String PATH = "wint.session.cookie.path";
        String EXPIRE = Constants.PropertyKeys.WINT_SESSION_EXPIRE;
        String COOKIE_ENCRYPTKEY_PATH = "wint.session.cookie.key.file";
    }

    interface DefaultValues {
        boolean ENCRYPT = true;
        String ENCRYPT_KEY = "TestWintKey";
        String DATA_SEPARATE = ";";
        int DATA_MAX_SIZE = 1024 * 3;
        int COOKIE_DATA_INDEX = 0;
        String COOKIE_DATA_PREFIX = "w_data_";
        String DOMAIN = "";
        String PATH = "/";
        int EXPIRE = Constants.Defaults.WINT_SESSION_EXPIRE;
        String COOKIE_ENCRYPTKEY_PATH = "wint-session-key";
    }

}
