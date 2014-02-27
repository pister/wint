package wint.sessionx.provider.cookie;

import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.sessionx.cookie.WintCookie;
import wint.sessionx.constants.AttrKeys;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.servlet.WintSessionHttpServletResponse;
import wint.sessionx.store.AbstractSessionStore;
import wint.sessionx.store.SessionData;

import java.util.Map;
import java.util.Set;

/**
 * Created by pister on 14-2-26.
 */
public class CookieSessionStore extends AbstractSessionStore {

    private Map<String, SessionData> orgData = MapUtil.newHashMap();

    private Set<String> toBeDeleteNames = CollectionUtil.newHashSet();

    private Map<String, SessionData> updatedData = MapUtil.newHashMap();

    private CookieCodec cookieCodec;

    public CookieSessionStore(CookieCodec cookieCodec) {
        this.cookieCodec = cookieCodec;
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

    public void commit(FilterContext filterContext) {
        WintCookie cookie = cookieCodec.buildCookie(0, this);
        if (cookie == null) {
            return;
        }
        WintSessionHttpServletResponse response = (WintSessionHttpServletResponse)filterContext.getAttribute(AttrKeys.NEW_RESPONSE);
        response.addWintCookie(cookie);
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
}
