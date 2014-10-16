package wint.sessionx.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.lang.WintException;
import wint.lang.magic.MagicClass;
import wint.lang.utils.MapUtil;
import wint.sessionx.constants.DefaultSupportTypes;
import wint.sessionx.provider.cookie.CookieSessionProvider;
import wint.sessionx.provider.redis.RedisSessionProvider;

import java.util.Map;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午2:27
 */
public class SessionProviderFactory {


    private static final Map<String, Class<? extends SessionProvider>> namedProviders = MapUtil.newHashMap();

    static {
        namedProviders.put(DefaultSupportTypes.COOKIE, CookieSessionProvider.class);
        namedProviders.put(DefaultSupportTypes.REDIS, RedisSessionProvider.class);
    }

    public static SessionProvider getSessionProvider(String type) {
        Class<? extends SessionProvider> clazz = namedProviders.get(type);
        if (clazz != null) {
            return (SessionProvider) MagicClass.wrap(clazz).newInstance().getObject();
        }

        MagicClass providerClass = MagicClass.forName(type);
        if (providerClass.isAssignableTo(SessionProvider.class)) {
            return (SessionProvider) providerClass.newInstance().getObject();
        }
        throw new WintException(providerClass + " can not be assign to SessionProvider");
    }

}
