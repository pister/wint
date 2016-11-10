package wint.sessionx.provider.batch;

import wint.sessionx.store.SessionData;

/**
 * User: huangsongli
 * Date: 16/11/10
 * Time: 下午5:20
 */
public abstract class AbstractSessionAttributeWriter implements SessionAttributeWriter {

    @Override
    public void setString(String name, String value) {
        set(name, new SessionData(name, value));
    }

    @Override
    public void setLong(String name, Long value) {
        set(name, new SessionData(name, value));
    }

    @Override
    public void setBoolean(String name, Boolean value) {
        set(name, new SessionData(name, value));

    }

    @Override
    public void setInteger(String name, Integer value) {
        set(name, new SessionData(name, value));
    }
}
