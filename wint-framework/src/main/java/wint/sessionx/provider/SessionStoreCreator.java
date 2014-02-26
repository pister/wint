package wint.sessionx.provider;

import wint.sessionx.store.SessionStore;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午2:09
 */
public interface SessionStoreCreator {

    SessionStore createSessionStore(Object parseRequestResult);

}
