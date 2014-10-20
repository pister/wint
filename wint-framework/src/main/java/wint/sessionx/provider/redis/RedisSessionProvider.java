package wint.sessionx.provider.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import wint.lang.convert.ConvertUtil;
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
        redisSessionStoreCreator = new RedisSessionStoreCreator(redisSessionConfig, initJedisPool(redisSessionConfig));
        servletContext.log("Wint redis session provider has been initialized.");
    }

    protected JedisPool initJedisPool(RedisSessionConfig redisSessionConfig) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(80);
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMaxWaitMillis(1000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);

        String serverAddress = redisSessionConfig.getRedisServerAddress();
        String[] parts = serverAddress.split(":");
        if (parts.length < 2) {
            throw new RuntimeException("invalidate redis server address format: " + serverAddress);
        }
        String host = parts[0];
        int port = ConvertUtil.toInt(parts[1], -1);
        if (port == -1) {
            throw new RuntimeException("invalidate redis server address format: " + serverAddress);
        }
        return new JedisPool(jedisPoolConfig, host, port);
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
