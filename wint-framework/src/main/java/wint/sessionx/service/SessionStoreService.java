package wint.sessionx.service;

import wint.core.service.Service;
import wint.sessionx.store.SessionStore;

/**
 * User: huangsongli
 * Date: 16/3/5
 * Time: 下午2:16
 */
public interface SessionStoreService extends Service {
    SessionStore getSessionStore(Object parseRequestResult);
}
