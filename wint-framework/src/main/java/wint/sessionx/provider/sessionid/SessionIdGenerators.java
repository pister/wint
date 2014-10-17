package wint.sessionx.provider.sessionid;

/**
 * User: huangsongli
 * Date: 14-10-17
 * Time: 上午11:47
 */
public abstract class SessionIdGenerators {

    public static SessionIdGenerator getSessionIdGenerator() {
        return new UuidSessionIdGenerator();
    }

}
