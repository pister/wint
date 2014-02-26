package wint.sessionx.store;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:33
 */
public interface SessionStore {

    SessionEntry get(String name);

    void set(String name, SessionEntry sessionEntry);

    void delete(String name);

}
