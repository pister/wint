package wint.sessionx.provider.batch;

import wint.sessionx.provider.cookie.CookieSessionStore;
import wint.sessionx.store.SessionData;

/**
 * User: huangsongli
 * Date: 16/11/10
 * Time: 下午5:26
 */
public class CookieSessionAttributeWriter extends AbstractSessionAttributeWriter {

    private CookieSessionStore cookieSessionStore;

    public CookieSessionAttributeWriter(CookieSessionStore cookieSessionStore) {
        this.cookieSessionStore = cookieSessionStore;
    }

    @Override
    public void set(String name, SessionData sessionData) {
        cookieSessionStore.set(name, sessionData);
    }
}
