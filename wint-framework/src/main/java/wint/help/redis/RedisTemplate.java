package wint.help.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCommands;
import redis.clients.util.Pool;

/**
 * User: huangsongli
 * Date: 15/1/7
 * Time: 下午2:08
 */
public class RedisTemplate {

    private static final Logger log = LoggerFactory.getLogger(RedisTemplate.class);
    private Pool<JedisCommands> jedisPool;

    public RedisTemplate(Pool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public Pool<JedisCommands> getJedisPool() {
        return jedisPool;
    }

    public <T> T execute(RedisCommand<T> command) {
        JedisCommands jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            T object = command.doInExec(jedis);
            jedisPool.returnResource(jedis);
            return object;
        } catch (Exception e) {
            log.error("execute error", e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        }
    }

    public void executeNoResult(final RedisCommandNoResult command) {
        execute(new RedisCommand<Object>() {
            @Override
            public Object doInExec(JedisCommands commands) {
                command.doInExec(commands);
                return null;
            }
        });
    }

    public void close() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }

}
