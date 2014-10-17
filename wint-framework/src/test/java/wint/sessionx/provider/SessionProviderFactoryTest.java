package wint.sessionx.provider;

import junit.framework.TestCase;
import wint.sessionx.constants.DefaultSupportTypes;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午2:36
 */
public class SessionProviderFactoryTest extends TestCase {

    public void testGetProvider() {
        SessionProvider sessionProvider = SessionProviderFactory.getSessionProvider(DefaultSupportTypes.COOKIE);
        TestCase.assertNotNull(sessionProvider);
    }

}
