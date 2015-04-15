package wint.sessionx.provider.redis;

import redis.clients.jedis.JedisCommands;
import wint.help.redis.RedisClient;
import wint.help.redis.RedisCommand;
import wint.help.redis.RedisCommandNoResult;
import wint.lang.utils.StringUtil;
import wint.sessionx.cookie.WintCookie;
import wint.sessionx.provider.BaseConfig;
import wint.sessionx.provider.sessionid.SessionIdGenerator;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.store.AbstractSessionStore;
import wint.sessionx.store.SessionData;

import java.util.Set;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午3:31
 */
public class RedisSessionStore extends AbstractSessionStore {

    private RedisClient redisClient;
    private SerializeService serializeService;
    private String sessionId;
    private RedisSessionConfig config;
    private SessionIdGenerator sessionIdGenerator;

    public RedisSessionStore(RedisClient redisClient, SerializeService serializeService, RedisSessionConfig config, SessionIdGenerator sessionIdGenerator, String sessionId) {
        this.redisClient = redisClient;
        this.serializeService = serializeService;
        this.config = config;
        this.sessionIdGenerator = sessionIdGenerator;
        this.sessionId = sessionId;
    }

    protected String getRedisKey() {
        if (StringUtil.isEmpty(config.getRedisKeyPrefix())) {
            return sessionId;
        }
        return config.getRedisKeyPrefix() + sessionId;
    }

    @Override
    public SessionData get(final String name) {
        return redisClient.getRedisTemplate().execute(new RedisCommand<SessionData>() {
            @Override
            public SessionData doInExec(JedisCommands commands) {
                String data = commands.hget(getRedisKey(), name);
                return (SessionData) serializeService.unserialize(data);
            }
        });
    }

    @Override
    public void set(final String name, final SessionData sessionData) {
        redisClient.getRedisTemplate().executeNoResult(new RedisCommandNoResult() {
            @Override
            public void doInExec(JedisCommands commands) {
                String data = (String) serializeService.serialize(sessionData);
                commands.hset(getRedisKey(), name, data);
            }
        });
    }

    @Override
    public void remove(final String name) {
        redisClient.getRedisTemplate().executeNoResult(new RedisCommandNoResult() {
            @Override
            public void doInExec(JedisCommands commands) {
                commands.hdel(getRedisKey(), name);
            }
        });
    }

    @Override
    public void clearAll() {
        redisClient.getRedisTemplate().executeNoResult(new RedisCommandNoResult() {
            @Override
            public void doInExec(JedisCommands commands) {
                commands.del(getRedisKey());
            }
        });
    }

    @Override
    public WintCookie commitForCookie() {
        redisClient.getRedisTemplate().executeNoResult(new RedisCommandNoResult() {
            @Override
            public void doInExec(JedisCommands commands) {
                String key = getRedisKey();
                if (commands.exists(key)) {
                    commands.expire(key, config.getExpire());
                }
            }
        });

        WintCookie cookie = new WintCookie(config.getSessionIdName(), sessionId);
        String domain = config.getDomain();
        if (!StringUtil.isEmpty(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setHttpOnly(true);
        // 设置为非持久化cookie
        //  cookie.setMaxAge(config.getExpire());
        cookie.setPath(config.getPath());
        return cookie;
    }

    @Override
    public Set<String> getNames() {
        return redisClient.getRedisTemplate().execute(new RedisCommand<Set<String>>() {
            @Override
            public Set<String> doInExec(JedisCommands commands) {
                return commands.hkeys(getRedisKey());
            }
        });
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    protected BaseConfig getConfig() {
        return config;
    }

    protected SessionIdGenerator getSessionIdGenerator() {
        return sessionIdGenerator;
    }
}