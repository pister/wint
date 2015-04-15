package wint.help.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: huangsongli
 * Date: 15/4/14
 * Time: 上午11:34
 */
public class SentinelRedisClient extends AbstractRedisClient<Jedis> {

    // sentinel://mymaster@127.0.0.1:6379;127.0.0.1:6379
    private static final String DEFAULT_MASTER_NAME = "mymaster";

    @Override
    protected Pool<Jedis> getPool(JedisPoolConfig jedisPoolConfig, String serverAddress) {
        String[] hosts = serverAddress.split(";");
        String host0 = hosts[0];
        String[] masterNameAndHost0 = host0.split("@");
        String masterName;
        if (masterNameAndHost0.length < 2) {
            masterName = DEFAULT_MASTER_NAME;
        } else {
            masterName = masterNameAndHost0[0];
            hosts[0] = masterNameAndHost0[1];
        }

        Set<String> sentinels = new HashSet<String>(Arrays.asList(hosts));
        return new JedisSentinelPool(masterName, sentinels, jedisPoolConfig);
    }

}
