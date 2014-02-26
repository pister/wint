package wint.sessionx.provider.cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.lang.utils.MapUtil;
import wint.sessionx.provider.RequestParser;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.store.SessionData;
import wint.sessionx.util.SessionIdGenerator;

import javax.servlet.http.Cookie;
import java.util.Map;

/**
 * Created by pister on 14-2-26.
 */
public class CookieRequestParser implements RequestParser {

    private CookieSessionConfig config;

    private SerializeService serializeService;

    private CookieCodec cookieCodec;

    public CookieRequestParser(CookieSessionConfig config, SerializeService serializeService, CookieCodec cookieCodec) {
        this.config = config;
        this.serializeService = serializeService;
        this.cookieCodec = cookieCodec;
    }

    public Object parseRequest(Cookie[] cookies) {
        CookieSessionStore cookieSessionStore = new CookieSessionStore();
        Map<String, SessionData> data = MapUtil.newHashMap();
        for (Cookie cookie : cookies) {
            String value = cookie.getValue();
            Map<String, SessionData> dataResult = cookieCodec.parseData(value);
            if (dataResult != null) {
                data.putAll(dataResult);
            }
        }
        SessionData sessionLastAccessData = data.remove(CookieContants.LAST_ACCESS_TIME_NAME);
        if (sessionLastAccessData != null) {
            Long lastAccessTime = (Long)sessionLastAccessData.getData();
            if (lastAccessTime != null) {
                cookieSessionStore.setLastAccessTime(lastAccessTime);
            }
        }

        SessionData sessionIdData = data.remove(CookieContants.SESSION_ID_ATTRIBUTE_NAME);
        if (sessionIdData != null) {
            cookieSessionStore.setSessionId((String)sessionIdData.getData());
        } else {
            cookieSessionStore.setSessionId(SessionIdGenerator.generateSessionId());
        }
        return cookieSessionStore;
    }


}
