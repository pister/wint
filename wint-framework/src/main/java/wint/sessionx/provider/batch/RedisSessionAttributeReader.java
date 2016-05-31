package wint.sessionx.provider.batch;

import redis.clients.jedis.JedisCommands;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.store.SessionData;

/**
 * User: huangsongli
 * Date: 16/5/27
 * Time: 下午1:10
 */
public class RedisSessionAttributeReader extends AbstractSessionAttributeReader implements SessionAttributeReader {

    private JedisCommands commands;

    private String redisKey;

    private SerializeService serializeService;

    public RedisSessionAttributeReader(JedisCommands commands, String redisKey, SerializeService serializeService) {
        this.commands = commands;
        this.redisKey = redisKey;
        this.serializeService = serializeService;
    }

    @Override
    public SessionData get(String name) {
        String data = commands.hget(redisKey, name);
        return (SessionData) serializeService.unserialize(data);
    }
}
