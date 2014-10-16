package wint.sessionx.provider.redis;

import wint.lang.magic.MagicMap;
import wint.sessionx.provider.RequestParser;
import wint.sessionx.provider.SessionProvider;
import wint.sessionx.provider.SessionStoreCreator;

import javax.servlet.ServletContext;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午2:41
 */
public class RedisSessionProvider implements SessionProvider {
    @Override
    public void init(MagicMap initParameters, ServletContext servletContext) {
    }

    @Override
    public RequestParser getRequestParser() {
        return null;
    }

    @Override
    public SessionStoreCreator getSessionStoreCreator() {
        return null;
    }
}
