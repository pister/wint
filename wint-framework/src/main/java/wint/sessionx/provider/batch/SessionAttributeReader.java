package wint.sessionx.provider.batch;

import wint.sessionx.store.SessionData;

/**
 * User: huangsongli
 * Date: 16/5/27
 * Time: 下午1:09
 */
public interface SessionAttributeReader {

    SessionData get(String name);

    String getString(String name);

    Long getLong(String name);

    Boolean getBoolean(String name);

    Integer getInteger(String name);
}
