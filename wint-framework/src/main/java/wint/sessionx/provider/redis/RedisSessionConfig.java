package wint.sessionx.provider.redis;

import wint.lang.magic.MagicMap;
import wint.sessionx.provider.BaseConfig;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午5:03
 */
public class RedisSessionConfig extends BaseConfig {

    private int redisDb;

    private String redisServerAddress;

    private String sessionIdName;

    public RedisSessionConfig(MagicMap properties) {
        super(properties);
        redisDb = properties.getInt(RedisConstants.PropertyKeys.REDIS_DB, RedisConstants.DefaultValues.REDIS_DB);
        sessionIdName = properties.getString(RedisConstants.PropertyKeys.SESSION_ID_NAME, RedisConstants.DefaultValues.SESSION_ID_NAME);
        redisServerAddress = properties.getString(RedisConstants.PropertyKeys.REDIS_SERVER_ADDRESS, RedisConstants.DefaultValues.REDIS_SERVER_ADDRESS);
    }

    public int getRedisDb() {
        return redisDb;
    }

    public String getSessionIdName() {
        return sessionIdName;
    }

    public String getRedisServerAddress() {
        return redisServerAddress;
    }
}
