package wint.sessionx.filter.filters;

import wint.core.config.Constants;
import wint.lang.magic.MagicMap;
import wint.sessionx.constants.AttrKeys;
import wint.sessionx.constants.SpecSessionKeys;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.provider.sessionid.SessionIdGenerator;
import wint.sessionx.store.SessionStore;

/**
 * User: longyi
 * Date: 14-2-27
 * Time: 下午12:57
 */
public class EnsureStatusFilter extends AbstractFilter {

    private int expireInSeconds;

    @Override
    public void init(MagicMap initParamters) {
        super.init(initParamters);
        expireInSeconds = initParamters.getInt(Constants.PropertyKeys.WINT_SESSION_EXPIRE, Constants.Defaults.WINT_SESSION_EXPIRE);
    }

    protected boolean isSessionExpire(int maxInactiveInterval, long lastAccessedTime) {
        return (System.currentTimeMillis() - lastAccessedTime) > (maxInactiveInterval * 1000);
    }

    private void resetSessionStore(SessionStore sessionStore, FilterContext filterContext) {
        sessionStore.clearAll();
        MagicMap magicMap = MagicMap.newMagicMap();

        SessionIdGenerator sessionIdGenerator = (SessionIdGenerator) filterContext.getAttribute(AttrKeys.SESSION_ID_GENERATOR);
        String newSessionId = sessionIdGenerator.generateSessionId();
        magicMap.put(SpecSessionKeys.SESSION_ID, newSessionId);
        magicMap.put(SpecSessionKeys.MAX_INACTIVE_INTERVAL, expireInSeconds);
        magicMap.put(SpecSessionKeys.CREATE_TIME, System.currentTimeMillis());
        magicMap.put(SpecSessionKeys.LAST_ACCESSED_TIME, System.currentTimeMillis());

        sessionStore.init(magicMap);
    }

    public void doFilter(FilterContext filterContext) {
        SessionStore sessionStore = (SessionStore) filterContext.getAttribute(AttrKeys.SESSION_STORE);

        String sessionId = sessionStore.getString(SpecSessionKeys.SESSION_ID);
        Long lastAccessedTime = sessionStore.getLong(SpecSessionKeys.LAST_ACCESSED_TIME);
        Integer maxInactiveInterval = sessionStore.getInteger(SpecSessionKeys.MAX_INACTIVE_INTERVAL);
        Long createTime = sessionStore.getLong(SpecSessionKeys.CREATE_TIME);

        if (sessionId == null || lastAccessedTime == null || maxInactiveInterval == null || createTime == null) {
            // init new session
            resetSessionStore(sessionStore, filterContext);
        }  else if (isSessionExpire(maxInactiveInterval, lastAccessedTime)) {
            // expire
            resetSessionStore(sessionStore, filterContext);
        } else {
            // update access time
            sessionStore.setData(SpecSessionKeys.LAST_ACCESSED_TIME, System.currentTimeMillis());
        }

        filterContext.invokeNext();
    }

}
