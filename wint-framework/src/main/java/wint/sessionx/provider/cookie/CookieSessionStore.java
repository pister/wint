package wint.sessionx.provider.cookie;

import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.sessionx.constants.SpecSessionKeys;
import wint.sessionx.cookie.WintCookie;
import wint.sessionx.provider.BaseConfig;
import wint.sessionx.provider.batch.BatchGetReceiver;
import wint.sessionx.provider.batch.BatchSetter;
import wint.sessionx.provider.batch.CookieSessionAttributeReader;
import wint.sessionx.provider.batch.CookieSessionAttributeWriter;
import wint.sessionx.provider.sessionid.SessionIdGenerator;
import wint.sessionx.store.AbstractSessionStore;
import wint.sessionx.store.SessionData;

import java.util.Map;
import java.util.Set;

/**
 * Created by pister on 14-2-26.
 */
public class CookieSessionStore extends AbstractSessionStore {

    private CookieSessionConfig config;
    private SessionIdGenerator sessionIdGenerator;
    private Map<String, SessionData> orgData = MapUtil.newHashMap();
    private Set<String> toBeDeleteNames = CollectionUtil.newHashSet();
    private Map<String, SessionData> updatedData = MapUtil.newHashMap();
    private CookieCodec cookieCodec;

    public CookieSessionStore(CookieCodec cookieCodec, CookieSessionConfig config, SessionIdGenerator sessionIdGenerator) {
        this.cookieCodec = cookieCodec;
        this.config = config;
        this.sessionIdGenerator = sessionIdGenerator;
    }

    @Override
    public String getSessionId() {
        return getString(SpecSessionKeys.SESSION_ID);
    }

    public void set(String name, SessionData data) {
        updatedData.put(name, data);
        toBeDeleteNames.remove(name);
    }

    public SessionData get(String name) {
        if (toBeDeleteNames.contains(name)) {
            return null;
        }
        SessionData ret = updatedData.get(name);
        if (ret != null) {
            return ret;
        }
        return orgData.get(name);
    }

    @Override
    public void batchGet(BatchGetReceiver batchGetReceiver) {
        batchGetReceiver.onReceive(new CookieSessionAttributeReader(this));
    }

    @Override
    public void batchSet(BatchSetter batchSetter) {
        batchSetter.onSet(new CookieSessionAttributeWriter(this));
    }

    public void remove(String name) {
        updatedData.remove(name);
        toBeDeleteNames.add(name);
    }

    public Set<String> getNames() {
        Set<String> ret = CollectionUtil.newHashSet();
        ret.addAll(orgData.keySet());
        ret.addAll(updatedData.keySet());
        ret.removeAll(toBeDeleteNames);
        return ret;
    }

    public void clearAll() {
        orgData.clear();
        toBeDeleteNames.clear();
        updatedData.clear();
    }

    public WintCookie commitForCookie() {
        return commitForCookie(0);
    }

    @Override
    public WintCookie commitForCookie(int expire) {
        return cookieCodec.buildCookie(this);
    }

    public Map<String, SessionData> getOrgData() {
        return orgData;
    }

    public void initData(Map<String, SessionData> data) {
        this.orgData.putAll(data);
    }

    public Set<String> getToBeDeleteNames() {
        return toBeDeleteNames;
    }

    public Map<String, SessionData> getUpdatedData() {
        return updatedData;
    }

    @Override
    protected BaseConfig getConfig() {
        return config;
    }

    @Override
    protected SessionIdGenerator getSessionIdGenerator() {
        return sessionIdGenerator;
    }
}
