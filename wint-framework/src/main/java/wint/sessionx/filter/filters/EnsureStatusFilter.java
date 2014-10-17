package wint.sessionx.filter.filters;

import wint.sessionx.constants.AttrKeys;
import wint.sessionx.constants.SpecSessionKeys;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.store.SessionStore;

/**
 * User: longyi
 * Date: 14-2-27
 * Time: 下午12:57
 */
public class EnsureStatusFilter extends AbstractFilter {

    protected boolean isSessionExpire(int maxInactiveInterval, long lastAccessedTime) {
        return (System.currentTimeMillis() - lastAccessedTime) > (maxInactiveInterval * 1000);
    }

    public void doFilter(FilterContext filterContext) {
        SessionStore sessionStore = (SessionStore) filterContext.getAttribute(AttrKeys.SESSION_STORE);

        String sessionId = sessionStore.getSessionId();
        Long lastAccessedTime = sessionStore.getLong(SpecSessionKeys.LAST_ACCESSED_TIME);
        Integer maxInactiveInterval = sessionStore.getInteger(SpecSessionKeys.MAX_INACTIVE_INTERVAL);
        Long createTime = sessionStore.getLong(SpecSessionKeys.CREATE_TIME);

        if (sessionId == null || lastAccessedTime == null || maxInactiveInterval == null || createTime == null) {
            // init new session
            sessionStore.reset();
        } else if (isSessionExpire(maxInactiveInterval, lastAccessedTime)) {
            // expire
            sessionStore.reset();
        } else {
            // update access time
            sessionStore.setData(SpecSessionKeys.LAST_ACCESSED_TIME, System.currentTimeMillis());
        }

        filterContext.invokeNext();
    }

}
