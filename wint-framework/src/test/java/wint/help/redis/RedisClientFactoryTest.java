package wint.help.redis;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by songlihuang on 2018/10/12.
 */
public class RedisClientFactoryTest extends TestCase {
    public void testSentinelClient() throws Exception {
        {
            RedisClientFactory redisClientFactory = new RedisClientFactory();
            redisClientFactory.setServerAddress("sentinel://mymaster/2@192.168.1.226:26379;192.168.1.226:26379");
            RedisClient redisClient = redisClientFactory.getRedisClient();
            if (redisClient instanceof SentinelRedisClient) {
                SentinelRedisClient sentinelRedisClient = (SentinelRedisClient) redisClient;
                Assert.assertEquals(3, sentinelRedisClient.getDatabase());
            } else {
                Assert.fail("must be a SentinelRedisClient!");
            }
        }
        {
            RedisClientFactory redisClientFactory = new RedisClientFactory();
            redisClientFactory.setServerAddress("sentinel://mymaster@192.168.1.226:26379;192.168.1.226:26379");
            RedisClient redisClient = redisClientFactory.getRedisClient();
            if (redisClient instanceof SentinelRedisClient) {
                SentinelRedisClient sentinelRedisClient = (SentinelRedisClient) redisClient;
                Assert.assertEquals(0, sentinelRedisClient.getDatabase());
            } else {
                Assert.fail("must be a SentinelRedisClient!");
            }
        }
    }

    public void testGetDefaultRedisClient() throws Exception {
        {
            RedisClientFactory redisClientFactory = new RedisClientFactory();
            redisClientFactory.setServerAddress("redis://192.168.1.226:6379;192.168.1.226:6379/3");
            RedisClient redisClient = redisClientFactory.getRedisClient();
            if (redisClient instanceof DefaultRedisClient) {
                DefaultRedisClient sentinelRedisClient = (DefaultRedisClient) redisClient;
                Assert.assertEquals(3, sentinelRedisClient.getDatabase());
            } else {
                Assert.fail("must be a DefaultRedisClient!");
            }

        }
        {
            RedisClientFactory redisClientFactory = new RedisClientFactory();
            redisClientFactory.setServerAddress("redis://192.168.1.226:6379;192.168.1.226:6379");
            RedisClient redisClient = redisClientFactory.getRedisClient();
            if (redisClient instanceof DefaultRedisClient) {
                DefaultRedisClient sentinelRedisClient = (DefaultRedisClient)redisClient;
                Assert.assertEquals(0, sentinelRedisClient.getDatabase());
            } else {
                Assert.fail("must be a DefaultRedisClient!");
            }
        }

    }

}