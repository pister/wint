package wint.sessionx.provider.redis;

import wint.help.redis.RedisClient;
import wint.help.redis.RedisClientFactory;
import wint.lang.magic.MagicMap;
import wint.sessionx.provider.RequestParser;
import wint.sessionx.provider.SessionProvider;
import wint.sessionx.provider.SessionStoreCreator;

import javax.servlet.ServletContext;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午2:41
 */
public class RedisSessionProvider implements SessionProvider {

    private RedisRequestParser redisRequestParser;

    private RedisSessionStoreCreator redisSessionStoreCreator;

    private RedisSessionConfig redisSessionConfig;

    @Override
    public void init(MagicMap initParameters, ServletContext servletContext) {
        redisSessionConfig = new RedisSessionConfig(initParameters);
        redisRequestParser = new RedisRequestParser(redisSessionConfig);
        redisSessionStoreCreator = new RedisSessionStoreCreator(redisSessionConfig, initRedisClient(redisSessionConfig));
        servletContext.log("Wint redis session provider has been initialized.");
    }

    protected RedisClient initRedisClient(RedisSessionConfig redisSessionConfig) {
        RedisClientFactory redisClientFactory = new RedisClientFactory();
        redisClientFactory.setMaxTotal(80);
        redisClientFactory.setMaxIdle(5);
        redisClientFactory.setMinIdle(1);

        String serverAddress = redisSessionConfig.getRedisServerAddress();
        redisClientFactory.setServerAddress(serverAddress);
        return redisClientFactory.getRedisClient();
    }

    @Override
    public RequestParser getRequestParser() {
        return redisRequestParser;
    }

    @Override
    public SessionStoreCreator getSessionStoreCreator() {
        return redisSessionStoreCreator;
    }
}
