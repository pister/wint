package wint.help.redis;

import redis.clients.jedis.BinaryJedisCommands;

/**
 * Created by songlihuang on 2017/6/23.
 */
public interface BinaryRedisCommandNoResult {

    void doInExec(BinaryJedisCommands commands);

}
