package wint.sessionx.store;

import wint.lang.magic.MagicMap;
import wint.sessionx.cookie.WintCookie;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.provider.batch.BatchGetReceiver;

import java.util.Set;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:33
 */
public interface SessionStore {

    String getSessionId();

    SessionData get(String name);

    void set(String name, SessionData sessionData);

    void remove(String name);

    void clearAll();

    WintCookie commitForCookie();

    String getString(String name);

    Long getLong(String name);

    Boolean getBoolean(String name);

    Integer getInteger(String name);

    void setData(String name, Object data);

    Object getData(String name);

    Set<String> getNames();

    void invalidate();

    void init(MagicMap initProperties);

    boolean isNew();

    void reset();

    WintCookie commitForCookie(int expire);

    void batchGet(BatchGetReceiver batchGetReceiver);
}
