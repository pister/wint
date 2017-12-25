package wint.help.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

/**
 * User: huangsongli
 * Date: 15/1/29
 * Time: 上午11:11
 */
public class DefaultRedisClient extends AbstractRedisClient<Jedis> {
    @Override
    protected Pool<Jedis> getPool(JedisPoolConfig jedisPoolConfig, String serverAddress) {
        String[] hostAndPort = serverAddress.split(";")[0].split(":");
        String host = hostAndPort[0];
        int port = Integer.parseInt(hostAndPort[1]);
        return new JedisPool(jedisPoolConfig, host, port, timeout);
    }

}
