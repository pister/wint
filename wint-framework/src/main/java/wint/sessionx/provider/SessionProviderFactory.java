package wint.sessionx.provider;

import wint.lang.WintException;
import wint.lang.magic.MagicClass;
import wint.lang.utils.MapUtil;
import wint.sessionx.constants.DefaultSupportTypes;
import wint.sessionx.provider.cookie.CookieSessionProvider;

import java.util.Map;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午2:27
 */
public class SessionProviderFactory {


    private static final Map<String, String> namedProviders = MapUtil.newHashMap();

    static {
        namedProviders.put(DefaultSupportTypes.COOKIE, CookieSessionProvider.class.getName());
        namedProviders.put(DefaultSupportTypes.REDIS, "wint.sessionx.provider.redis.RedisSessionProvider");
    }

    public static SessionProvider getSessionProvider(String type) {
        String clazzName = namedProviders.get(type);
        if (clazzName == null) {
            clazzName = type;
        }
        MagicClass providerClass = MagicClass.forName(clazzName);
        if (providerClass.isAssignableTo(SessionProvider.class)) {
            return (SessionProvider) providerClass.newInstance().getObject();
        }
        throw new WintException(providerClass + " can not be assign to SessionProvider");
    }

}
