package wint.sessionx.store;

import wint.sessionx.filter.FilterContext;

import java.util.Set;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:33
 */
public interface SessionStore {

    SessionData get(String name);

    void set(String name, SessionData sessionData);

    void remove(String name);

    void rollback();

    void commit(FilterContext filterContext);

    String getString(String name);

    Long getLong(String name);

    Integer getInteger(String name);

    void setData(String name, Object data);

    Object getData(String name);

    Set<String> getNames();

    void invalidate();

    boolean isNew();
}
