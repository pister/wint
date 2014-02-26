package wint.sessionx.provider.cookie;

import wint.sessionx.provider.SessionStoreCreator;
import wint.sessionx.store.SessionStore;

/**
 * Created by pister on 14-2-26.
 */
public class CookieSessionStoreCreator implements SessionStoreCreator {

    public SessionStore createSessionStore(Object parseRequestResult) {
        return (CookieSessionStore)parseRequestResult;
    }
}
