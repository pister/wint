package wint.sessionx.store;

import wint.lang.magic.MagicMap;

import java.util.Map;
import java.util.Set;

/**
 * Created by pister on 14-2-26.
 */
public abstract class AbstractSessionStore implements SessionStore {

    private boolean isNew = false;

    public String getString(String name) {
        SessionData sessionData = this.get(name);
        if (sessionData == null) {
            return null;
        }
        return (String) sessionData.getData();
    }

    public Long getLong(String name) {
        SessionData sessionData = this.get(name);
        if (sessionData == null) {
            return null;
        }
        return (Long) sessionData.getData();
    }

    public Integer getInteger(String name) {
        SessionData sessionData = this.get(name);
        if (sessionData == null) {
            return null;
        }
        return (Integer) sessionData.getData();
    }

    public void setData(String name, Object data) {
        this.set(name, new SessionData(name, data));
    }

    public Object getData(String name) {
        SessionData sessionData = this.get(name);
        if (sessionData == null) {
            return null;
        }
        return sessionData.getData();
    }

    public void invalidate() {
        Set<String> names = getNames();
        for (String name : names) {
            remove(name);
        }
    }

    public boolean isNew() {
        return isNew;
    }

    public void init(MagicMap initProperties) {
        isNew = true;
        for (Map.Entry<String, Object> entry : initProperties.entrySet()) {
            setData(entry.getKey(), entry.getValue());
        }
    }
}
