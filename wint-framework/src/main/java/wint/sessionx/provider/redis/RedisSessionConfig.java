package wint.sessionx.provider.redis;

import wint.core.config.property.PropertiesMap;
import wint.lang.magic.MagicMap;
import wint.sessionx.provider.BaseConfig;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午5:03
 */
public class RedisSessionConfig extends BaseConfig {

    private String redisServerAddress;

    private String sessionIdName;

    private String redisKeyPrefix;

    public RedisSessionConfig(PropertiesMap properties) {
        super(properties);
        sessionIdName = properties.getString(RedisConstants.PropertyKeys.SESSION_ID_NAME, RedisConstants.DefaultValues.SESSION_ID_NAME);
        redisServerAddress = properties.getString(RedisConstants.PropertyKeys.REDIS_SERVER_ADDRESS, RedisConstants.DefaultValues.REDIS_SERVER_ADDRESS);
        redisKeyPrefix = properties.getString(RedisConstants.PropertyKeys.REDIS_KEY_PREFIX, RedisConstants.DefaultValues.REDIS_KEY_PREFIX);
    }

    public String getSessionIdName() {
        return sessionIdName;
    }

    public String getRedisServerAddress() {
        return redisServerAddress;
    }

    public String getRedisKeyPrefix() {
        return redisKeyPrefix;
    }
}
