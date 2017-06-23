package wint.help.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.util.Pool;

/**
 * User: huangsongli
 * Date: 15/1/7
 * Time: 下午2:08
 */
public class RedisTemplate {

    private static final Logger log = LoggerFactory.getLogger(RedisTemplate.class);
    private Pool<Jedis> jedisPool;

    public RedisTemplate(Pool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }

    public <T> T execute(final RedisCommand<T> command) {
        return executeImpl(new ExecuteCallback<T>() {
            @Override
            public T exec(Jedis jedis) {
                return command.doInExec(jedis);
            }
        });
    }

    interface ExecuteCallback<T> {
        T exec(Jedis jedis);
    }


    private <T> T executeImpl(ExecuteCallback<T> callback) {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            T object = callback.exec(jedis);
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


    public <T> T executeBinary(final BinaryRedisCommand<T> command) {
       return executeImpl(new ExecuteCallback<T>() {
           @Override
           public T exec(Jedis jedis) {
               return command.doInExec(jedis);
           }
       });
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

    public void executeBinaryNoResult(final BinaryRedisCommandNoResult command) {
        executeBinary(new BinaryRedisCommand<Object>() {
            @Override
            public Object doInExec(BinaryJedisCommands commands) {
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
