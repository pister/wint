package wint.sessionx.provider.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import wint.help.redis.PoolRedisClient;
import wint.lang.magic.MagicMap;
import wint.sessionx.provider.sessionid.SessionIdGenerator;
import wint.sessionx.provider.sessionid.SessionIdGenerators;
import wint.sessionx.serialize.JsonStringSerializeService;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.store.SessionData;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: huangsongli
 * Date: 14-10-17
 * Time: 上午8:59
 */
public class RedisSessionStoreTest {

    private SerializeService serializeService = new JsonStringSerializeService();

    public void testForBench() {
        JedisPool jedisPool = new JedisPool(initPoolConfig(), "127.0.0.1", 6379);
        RedisSessionConfig config = initRedisSessionConfig();
        SessionIdGenerator sessionIdGenerator = SessionIdGenerators.getSessionIdGenerator();
        String sessionId = "test-session-id-123";

        int benchTimes = 100000;

        long start = System.currentTimeMillis();
        for (int ct = 0; ct < benchTimes; ++ct) {
            RedisSessionStore redisSessionStore = new RedisSessionStore(new PoolRedisClient(jedisPool), serializeService, config, sessionIdGenerator, sessionId);
            setAttribute(redisSessionStore, "name1", "the first string object");
            setAttribute(redisSessionStore, "name2", 123);
            setAttribute(redisSessionStore, "name3", new Date());
            setAttribute(redisSessionStore, "name4", new ArrayList<String>().add("value1"));
            for (int i = 1; i <= 4; ++i) {
                getAttribute(redisSessionStore, "name" + i);
            }
            if (ct > 0 && ct % 1000 == 0) {
                System.out.println("finishing " + (8 * ct) + " times.");

                long end = System.currentTimeMillis();
                long ts = end - start;
                long operCount = ct * 8;
                long qps = operCount * 1000 / ts;
                System.out.println(qps);
            }
        }


    }

    private void setAttribute(RedisSessionStore redisSessionStore, String name, Object value) {
        redisSessionStore.set(name, new SessionData(name, value));
    }

    private Object getAttribute(RedisSessionStore redisSessionStore, String name) {
        SessionData sessionData = redisSessionStore.get(name);
        if (sessionData == null) {
            return null;
        }
        return sessionData.getData();
    }

    private JedisPoolConfig initPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 控制一个pool最多有多少个状态为idle的jedis实例
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxIdle(50);
        // 超时时间
        jedisPoolConfig.setMaxWaitMillis(1000);
        // 在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可用的；
        jedisPoolConfig.setTestOnBorrow(true);
        // 在还会给pool时，是否提前进行validate操作
        jedisPoolConfig.setTestOnReturn(true);
        return jedisPoolConfig;
    }

    private RedisSessionConfig initRedisSessionConfig() {
        MagicMap properties = MagicMap.newMagicMap();
        properties.put(RedisConstants.PropertyKeys.REDIS_DB, 2);
        RedisSessionConfig config = new RedisSessionConfig(properties);

        return config;

    }

}
