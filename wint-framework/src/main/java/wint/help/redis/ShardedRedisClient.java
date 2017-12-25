package wint.help.redis;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Pool;

import java.util.ArrayList;
import java.util.List;

/**
 * User: huangsongli
 * Date: 15/4/14
 * Time: 上午11:50
 */
public class ShardedRedisClient extends AbstractRedisClient<ShardedJedis> {

    private static final String DEFAULT_NAME = "instance-0";

    @Override
    protected Pool<ShardedJedis> getPool(JedisPoolConfig jedisPoolConfig, String serverAddress) {
        String[] hosts = serverAddress.split(";");
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(hosts.length);
        for (String host : hosts) {
            String[] parts = host.split(":");
            if (parts.length < 3) {
                shards.add(new JedisShardInfo(parts[0], Integer.parseInt(parts[1]), timeout, DEFAULT_NAME));
            } else {
                shards.add(new JedisShardInfo(parts[0], Integer.parseInt(parts[1]), timeout, parts[2]));
            }
        }
        return new ShardedJedisPool(jedisPoolConfig, shards);
    }
}
