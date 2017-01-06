package wint.sessionx.filter.filters;

import wint.sessionx.constants.AttrKeys;
import wint.sessionx.constants.SpecSessionKeys;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.provider.batch.BatchGetReceiver;
import wint.sessionx.provider.batch.SessionAttributeReader;
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

    private class SessionBatchGetReceiver implements BatchGetReceiver {

        private Long lastAccessedTime;

        private Integer maxInactiveInterval;

        private Long createTime;

        @Override
        public void onReceive(SessionAttributeReader sessionAttributeReader) {
            lastAccessedTime = sessionAttributeReader.getLong(SpecSessionKeys.LAST_ACCESSED_TIME);
            maxInactiveInterval = sessionAttributeReader.getInteger(SpecSessionKeys.MAX_INACTIVE_INTERVAL);
            createTime =  sessionAttributeReader.getLong(SpecSessionKeys.CREATE_TIME);
        }

        public Long getLastAccessedTime() {
            return lastAccessedTime;
        }

        public Integer getMaxInactiveInterval() {
            return maxInactiveInterval;
        }

        public Long getCreateTime() {
            return createTime;
        }
    }

    public void doFilter(FilterContext filterContext) {
        SessionStore sessionStore = (SessionStore) filterContext.getAttribute(AttrKeys.SESSION_STORE);

        SessionBatchGetReceiver sessionBatchGetReceiver = new SessionBatchGetReceiver();
        sessionStore.batchGet(sessionBatchGetReceiver);

        String sessionId = sessionStore.getSessionId();
        Long lastAccessedTime = sessionBatchGetReceiver.getLastAccessedTime();
        Integer maxInactiveInterval = sessionBatchGetReceiver.getMaxInactiveInterval();
        Long createTime = sessionBatchGetReceiver.getCreateTime();


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
