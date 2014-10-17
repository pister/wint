package wint.sessionx.provider.cookie;

import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.sessionx.provider.RequestParser;
import wint.sessionx.provider.sessionid.SessionIdGenerator;
import wint.sessionx.provider.sessionid.UuidSessionIdGenerator;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.store.SessionData;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Map;

/**
 * Created by pister on 14-2-26.
 */
public class CookieRequestParser implements RequestParser {

    private CookieSessionConfig config;

    private CookieCodec cookieCodec;

    private SessionIdGenerator sessionIdGenerator = new UuidSessionIdGenerator();

    public CookieRequestParser(CookieSessionConfig config, CookieCodec cookieCodec) {
        this.config = config;
        this.cookieCodec = cookieCodec;
    }

    private List<Cookie> filterDataCookies(Cookie[] cookies) {
        if (cookies == null) {
            return CollectionUtil.newArrayList();
        }
        String prefixName = config.getPrefixName();
        List<Cookie> dataCookies = CollectionUtil.newArrayList();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if (StringUtil.isEmpty(name)) {
                continue;
            }
            if (!name.startsWith(prefixName)) {
                continue;
            }
            dataCookies.add(cookie);
        }
        return dataCookies;
    }


    public Object parseRequest(Cookie[] inputCookies) {
        CookieSessionStore cookieSessionStore = new CookieSessionStore(cookieCodec, config, sessionIdGenerator);
        Map<String, SessionData> data = MapUtil.newHashMap();

        List<Cookie> cookies = filterDataCookies(inputCookies);

        for (Cookie cookie : cookies) {
            String value = cookie.getValue();
            Map<String, SessionData> dataResult = cookieCodec.parseData(value);
            if (dataResult != null) {
                data.putAll(dataResult);
            }
        }

        cookieSessionStore.initData(data);

        return cookieSessionStore;
    }


}
