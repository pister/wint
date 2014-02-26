package wint.sessionx.provider.cookie;

import wint.core.config.Constants;

/**
 * Created by pister on 14-2-26.
 */
public interface CookieContants {

    String SESSION_ID_ATTRIBUTE_NAME = "_wint_sid";

    String LAST_ACCESS_TIME_NAME = "_wint_xlast";

    String URL_ENCODE_CHARSET = Constants.Defaults.CHARSET_ENCODING;

    String WINT_COOKIE_MAGIC_TOKEN = "WINT:";

    interface SpecAttrKey {
        String CREATE_TIME = "cr_t";
        String SESSION_ID = "s_id";
        String LAST_ACCESSED_TIME = "last_t";
        String MAX_INACTIVE_INTERVAL = "mx_ac";
    }

    interface PropertyKeys {
        String ENCRYPT = "wint.sessionx.cookie.encrypt";
        String ENCRYPT_KEY = "wint.sessionx.cookie.encrypt.key";
        String DATA_SEPARATE = "wint.sessionx.cookie.data.separate";
        String DATA_MAX_SIZE = "wint.sessionx.cookie.data.maxsize";
        String COOKIE_DATA_PREFIX = "wint.sessionx.cookie.data.prefix";
        String DOMAIN = "wint.sessionx.cookie.domain";
        String PATH = "wint.sessionx.cookie.path";
        String EXPIRE = "wint.sessionx.cookie.expire";
    }

    interface DefaultValues {
        boolean ENCRYPT = true;
        String ENCRYPT_KEY = "TestWintKey";
        String DATA_SEPARATE = ";";
        int DATA_MAX_SIZE = 1024 * 3;
        String COOKIE_DATA_PREFIX = "w_data_";
        String DOMAIN = "";
        String PATH = "/";
        int EXPIRE = 30 * 60;
    }

}
