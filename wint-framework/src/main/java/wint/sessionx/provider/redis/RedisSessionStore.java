package wint.sessionx.provider.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import wint.lang.utils.StringUtil;
import wint.sessionx.constants.AttrKeys;
import wint.sessionx.cookie.WintCookie;
import wint.sessionx.filter.FilterContext;
import wint.sessionx.provider.BaseConfig;
import wint.sessionx.provider.sessionid.SessionIdGenerator;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.servlet.WintSessionHttpServletResponse;
import wint.sessionx.store.AbstractSessionStore;
import wint.sessionx.store.SessionData;

import java.util.List;
import java.util.Set;

/**
 * User: huangsongli
 * Date: 14-10-16
 * Time: 下午3:31
 */
public class RedisSessionStore extends AbstractSessionStore {

    private JedisPool jedisPool;
    private SerializeService serializeService;
    private String sessionId;
    private RedisSessionConfig config;
    private SessionIdGenerator sessionIdGenerator;

    public RedisSessionStore(JedisPool jedisPool, SerializeService serializeService, RedisSessionConfig config, SessionIdGenerator sessionIdGenerator, String sessionId) {
        this.jedisPool = jedisPool;
        this.serializeService = serializeService;
        this.config = config;
        this.sessionIdGenerator = sessionIdGenerator;
        this.sessionId = sessionId;
    }

    protected <T> T exec(Command<T> command) {
        Jedis jedis = this.jedisPool.getResource();
        try {
            return command.doInExec(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
            throw new RuntimeException(e);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    protected String getRedisKey() {
        if (StringUtil.isEmpty(config.getRedisKeyPrefix())) {
            return sessionId;
        }
        return config.getRedisKeyPrefix() + sessionId;
    }

    @Override
    public SessionData get(final String name) {
        return exec(new Command<SessionData>() {
            @Override
            public SessionData doInExec(Jedis jedis) {
                String data = jedis.hget(getRedisKey(), name);
                return (SessionData) serializeService.unserialize(data);
            }
        });
    }

    @Override
    public void set(final String name, final SessionData sessionData) {
        exec(new Command<Object>() {
            @Override
            public Object doInExec(Jedis jedis) {
                String data = (String) serializeService.serialize(sessionData);
                jedis.hset(getRedisKey(), name, data);
                return null;
            }
        });
    }

    @Override
    public void remove(final String name) {
        exec(new Command<Object>() {
            @Override
            public Object doInExec(Jedis jedis) {
                jedis.hdel(getRedisKey(), name);
                return null;
            }
        });
    }

    @Override
    public void clearAll() {
        exec(new Command<Object>() {
            @Override
            public Object doInExec(Jedis jedis) {
                jedis.del(getRedisKey());
                return null;
            }
        });
    }

    @Override
    public WintCookie commitForCookie() {
        exec(new Command<Object>() {
            @Override
            public Object doInExec(Jedis jedis) {
                String key = getRedisKey();
                if (jedis.exists(key)) {
                    jedis.expire(key, config.getExpire());
                }
                return null;
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
        return exec(new Command<Set<String>>() {
            @Override
            public Set<String> doInExec(Jedis jedis) {
                return jedis.hkeys(getRedisKey());
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

    protected static interface Command<T> {
        T doInExec(Jedis jedis);
    }
}
