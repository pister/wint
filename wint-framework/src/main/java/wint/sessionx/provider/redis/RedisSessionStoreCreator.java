package wint.sessionx.provider.redis;

import redis.clients.jedis.JedisPool;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;
import wint.sessionx.provider.SessionStoreCreator;
import wint.sessionx.provider.sessionid.SessionIdGenerator;
import wint.sessionx.provider.sessionid.SessionIdGenerators;
import wint.sessionx.serialize.JsonStringSerializeService;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.store.SessionStore;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午3:29
 */
public class RedisSessionStoreCreator implements SessionStoreCreator {

    private SessionIdGenerator sessionIdGenerator = SessionIdGenerators.getSessionIdGenerator();
    private SerializeService serializeService = new JsonStringSerializeService();
    private JedisPool jedisPool;
    private RedisSessionConfig config;

    public RedisSessionStoreCreator(RedisSessionConfig config, JedisPool jedisPool) {
        this.config = config;
        this.jedisPool = jedisPool;
    }

    @Override
    public SessionStore createSessionStore(Object parseRequestResult) {
        String sessionId = (String) parseRequestResult;
        if (StringUtil.isEmpty(sessionId)) {
            sessionId = sessionIdGenerator.generateSessionId();
        }
        return new RedisSessionStore(jedisPool, serializeService, config, sessionIdGenerator, sessionId);
    }


}
