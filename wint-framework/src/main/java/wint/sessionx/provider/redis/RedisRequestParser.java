package wint.sessionx.provider.redis;

import wint.lang.utils.StringUtil;
import wint.sessionx.provider.RequestParser;

import javax.servlet.http.Cookie;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午3:29
 */
public class RedisRequestParser implements RequestParser {

    private RedisSessionConfig redisSessionConfig;

    public RedisRequestParser(RedisSessionConfig redisSessionConfig) {
        this.redisSessionConfig = redisSessionConfig;
    }

    @Override
    public Object parseRequest(Cookie[] cookies) {
        return parseSessionId(cookies);
    }

    protected String parseSessionId(Cookie[] cookies) {
        Cookie sessionIdCookie = getSessionIdCookie(cookies);
        if (sessionIdCookie == null) {
            return null;
        }
        return sessionIdCookie.getValue();
    }

    private Cookie getSessionIdCookie(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (StringUtil.equals(redisSessionConfig.getSessionIdName(), cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

}
