package wint.sessionx.provider.cookie;

import wint.core.config.property.MagicPropertiesMap;
import wint.core.config.property.PropertiesMap;
import wint.lang.magic.MagicMap;
import wint.sessionx.provider.RequestParser;
import wint.sessionx.provider.SessionProvider;
import wint.sessionx.provider.SessionStoreCreator;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.serialize.StringSerializeService;

import javax.servlet.ServletContext;

/**
 * Created by pister on 14-2-26.
 */
public class CookieSessionProvider implements SessionProvider {

    private CookieRequestParser cookieRequestParser;

    private CookieSessionStoreCreator cookieSessionStoreCreator;

    private CookieSessionConfig config;

    private SerializeService serializeService;

    private CookieCodec cookieCodec;

    public void init(PropertiesMap initParameters, ServletContext servletContext) {
        config = new CookieSessionConfig(initParameters);
        serializeService = new StringSerializeService();
        cookieCodec = new CookieCodec(config, serializeService);
        cookieRequestParser = new CookieRequestParser(config, cookieCodec);
        cookieSessionStoreCreator = new CookieSessionStoreCreator();
    }

    public RequestParser getRequestParser() {
        return cookieRequestParser;
    }

    public SessionStoreCreator getSessionStoreCreator() {
        return cookieSessionStoreCreator;
    }
}
