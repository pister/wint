package wint.sessionx.provider.batch;

import wint.sessionx.provider.cookie.CookieSessionStore;
import wint.sessionx.store.SessionData;

/**
 * User: huangsongli
 * Date: 16/5/27
 * Time: 下午1:21
 */
public class CookieSessionAttributeReader extends AbstractSessionAttributeReader implements SessionAttributeReader {

    private CookieSessionStore cookieSessionStore;

    public CookieSessionAttributeReader(CookieSessionStore cookieSessionStore) {
        this.cookieSessionStore = cookieSessionStore;
    }

    @Override
    public SessionData get(String name) {
        return cookieSessionStore.get(name);
    }
}
