package wint.help.redis;

import redis.clients.jedis.JedisCommands;

/**
 * User: huangsongli
 * Date: 15/1/7
 * Time: 下午2:09
 */
public interface RedisCommand<T> {
    T doInExec(JedisCommands commands);
}
