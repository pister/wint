package wint.sessionx.provider;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午2:04
 */
public interface SessionProvider {

    void init();

    RequestParser getRequestParser();

    SessionStoreCreator getSessionStoreCreator();

}
