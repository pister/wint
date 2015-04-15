package wint.help.redis;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

/**
 * User: huangsongli
 * Date: 15/4/15
 * Time: 下午7:30
 */
public class PoolRedisClient implements RedisClient {

    private RedisTemplate redisTemplate;

    public PoolRedisClient(Pool<Jedis> pool) {
        this.redisTemplate = new RedisTemplate(pool);
    }

    @Override
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
