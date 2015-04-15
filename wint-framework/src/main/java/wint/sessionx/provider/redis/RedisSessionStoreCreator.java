package wint.sessionx.provider.redis;

import wint.help.redis.RedisClient;
import wint.lang.utils.StringUtil;
import wint.sessionx.provider.SessionStoreCreator;
import wint.sessionx.provider.sessionid.SessionIdGenerator;
import wint.sessionx.provider.sessionid.SessionIdGenerators;
import wint.sessionx.serialize.JsonStringSerializeService;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.store.SessionStore;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午3:29
 */
public class RedisSessionStoreCreator implements SessionStoreCreator {

    private SessionIdGenerator sessionIdGenerator = SessionIdGenerators.getSessionIdGenerator();
    private SerializeService serializeService = new JsonStringSerializeService();
    private RedisClient redisClient;
    private RedisSessionConfig config;

    public RedisSessionStoreCreator(RedisSessionConfig config, RedisClient redisClient) {
        this.config = config;
        this.redisClient = redisClient;
    }

    @Override
    public SessionStore createSessionStore(Object parseRequestResult) {
        String sessionId = (String) parseRequestResult;
        if (StringUtil.isEmpty(sessionId)) {
            sessionId = sessionIdGenerator.generateSessionId();
        }
        return new RedisSessionStore(redisClient, serializeService, config, sessionIdGenerator, sessionId);
    }


}
