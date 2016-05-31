package wint.sessionx.provider.batch;

import wint.sessionx.store.SessionData;

/**
 * User: huangsongli
 * Date: 16/5/27
 * Time: 下午1:48
 */
public abstract class AbstractSessionAttributeReader implements SessionAttributeReader {

    @Override
    public String getString(String name) {
        SessionData sessionData = this.get(name);
        if (sessionData == null) {
            return null;
        }
        return (String) sessionData.getData();
    }

    @Override
    public Long getLong(String name) {
        SessionData sessionData = this.get(name);
        if (sessionData == null) {
            return null;
        }
        return (Long) sessionData.getData();
    }

    @Override
    public Boolean getBoolean(String name) {
        SessionData sessionData = this.get(name);
        if (sessionData == null) {
            return null;
        }
        return (Boolean) sessionData.getData();
    }

    @Override
    public Integer getInteger(String name) {
        SessionData sessionData = this.get(name);
        if (sessionData == null) {
            return null;
        }
        return (Integer) sessionData.getData();
    }
}
