package wint.sessionx.filter.filters;

import wint.sessionx.constants.AttrKeys;
import wint.sessionx.filter.Filter;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.provider.SessionStoreCreator;
import wint.sessionx.store.SessionStore;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午2:08
 */
public class CreateSessionStoreFilter extends AbstractFilter {

    private SessionStoreCreator sessionStoreCreator;

    public CreateSessionStoreFilter(SessionStoreCreator sessionStoreCreator) {
        this.sessionStoreCreator = sessionStoreCreator;
    }

    public void doFilter(FilterContext filterContext) {
        Object parseRequest = filterContext.getAttribute(AttrKeys.PARSE_REQUEST_RESULT);
        SessionStore sessionStore = sessionStoreCreator.createSessionStore(parseRequest);
        filterContext.setAttribute(AttrKeys.SESSION_STORE, sessionStore);
        filterContext.invokeNext();
    }
}
