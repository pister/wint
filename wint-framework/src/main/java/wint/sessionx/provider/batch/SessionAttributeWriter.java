package wint.sessionx.provider.batch;

import wint.sessionx.store.SessionData;

/**
 * User: huangsongli
 * Date: 16/11/10
 * Time: 下午5:19
 */
public interface SessionAttributeWriter {

    void set(String name, SessionData sessionData);

    void setString(String name, String value);

    void setLong(String name, Long value);

    void setBoolean(String name, Boolean value);

    void setInteger(String name, Integer value);

}
