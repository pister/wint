package wint.sessionx.provider.redis;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午5:52
 */
public interface RedisConstants {

    interface PropertyKeys {
        String REDIS_DB = "wint.sessionx.redis.db";
        String SESSION_ID_NAME = "wint.sessionx.redis.session.id.name";
        String REDIS_SERVER_ADDRESS = "wint.sessionx.redis.server.address";
        String REDIS_KEY_PREFIX = "wint.sessionx.redis.key.prefix";
    }

    interface DefaultValues {
        int REDIS_DB = 1;
        String SESSION_ID_NAME = "w_sid";
        String REDIS_SERVER_ADDRESS = "127.0.0.1:6379";
        String REDIS_KEY_PREFIX = "w_sid_";
    }

}
