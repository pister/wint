package wint.sessionx.provider.batch;

import redis.clients.jedis.JedisCommands;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.store.SessionData;

/**
 * User: huangsongli
 * Date: 16/11/10
 * Time: 下午5:21
 */
public class RedisSessionAttributeWriter extends AbstractSessionAttributeWriter {
    private JedisCommands commands;

    private String redisKey;

    private SerializeService serializeService;

    public RedisSessionAttributeWriter(JedisCommands commands, String redisKey, SerializeService serializeService) {
        this.commands = commands;
        this.redisKey = redisKey;
        this.serializeService = serializeService;
    }

    @Override
    public void set(String name, SessionData sessionData) {
        String data = (String) serializeService.serialize(sessionData);
        commands.hset(redisKey, name, data);
    }
}
