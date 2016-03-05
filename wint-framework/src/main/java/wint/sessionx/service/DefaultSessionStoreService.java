package wint.sessionx.service;

import wint.core.service.AbstractService;
import wint.sessionx.provider.SessionStoreCreator;
import wint.sessionx.store.SessionStore;

/**
 * User: huangsongli
 * Date: 16/3/5
 * Time: 下午2:16
 */
public class DefaultSessionStoreService extends AbstractService implements SessionStoreService {

    private SessionStoreCreator sessionStoreCreator;

    public DefaultSessionStoreService(SessionStoreCreator sessionStoreCreator) {
        this.sessionStoreCreator = sessionStoreCreator;
    }

    @Override
    public SessionStore getSessionStore(Object parseRequestResult) {
        return sessionStoreCreator.createSessionStore(parseRequestResult);
    }
}
