package wint.sessionx.store;

import wint.lang.magic.MagicMap;
import wint.sessionx.constants.SpecSessionKeys;
import wint.sessionx.provider.BaseConfig;
import wint.sessionx.provider.batch.BatchSetter;
import wint.sessionx.provider.batch.SessionAttributeWriter;
import wint.sessionx.provider.sessionid.SessionIdGenerator;

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

    @Override
    public Boolean getBoolean(String name) {
        SessionData sessionData = this.get(name);
        if (sessionData == null) {
            return null;
        }
        return (Boolean) sessionData.getData();
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

    public void init(final MagicMap initProperties) {
        isNew = true;
        this.batchSet(new BatchSetter() {
            @Override
            public void onSet(SessionAttributeWriter sessionAttributeWriter) {
                for (Map.Entry<String, Object> entry : initProperties.entrySet()) {
                    sessionAttributeWriter.set(entry.getKey(), new SessionData(entry.getKey(), entry.getValue()));
                }
            }
        });
    }

    protected abstract BaseConfig getConfig();

    protected abstract SessionIdGenerator getSessionIdGenerator();

    public void reset() {
        clearAll();
        MagicMap magicMap = MagicMap.newMagicMap();

        String newSessionId = getSessionIdGenerator().generateSessionId();
        magicMap.put(SpecSessionKeys.SESSION_ID, newSessionId);
        magicMap.put(SpecSessionKeys.MAX_INACTIVE_INTERVAL, getConfig().getExpire());
        magicMap.put(SpecSessionKeys.CREATE_TIME, System.currentTimeMillis());
        magicMap.put(SpecSessionKeys.LAST_ACCESSED_TIME, System.currentTimeMillis());

        init(magicMap);
    }
}
