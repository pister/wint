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

    @Override
    public SessionData get(final String name) {
        return exec(new Command<SessionData>() {
            @Override
            public SessionData doInExec(Jedis jedis) {
                String data = jedis.hget(sessionId, name);
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
                jedis.hset(sessionId, name, data);
                return null;
            }
        });
    }

    @Override
    public void remove(final String name) {
        exec(new Command<Object>() {
            @Override
            public Object doInExec(Jedis jedis) {
                jedis.hdel(sessionId, name);
                return null;
            }
        });
    }

    @Override
    public void clearAll() {
        exec(new Command<Object>() {
            @Override
            public Object doInExec(Jedis jedis) {
                jedis.del(sessionId);
                return null;
            }
        });
    }

    @Override
    public void commit(FilterContext filterContext) {
        exec(new Command<Object>() {
            @Override
            public Object doInExec(Jedis jedis) {
                if (jedis.exists(sessionId)) {
                    jedis.expire(sessionId, config.getExpire());
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
        cookie.setMaxAge(config.getExpire());
        cookie.setPath(config.getPath());
        WintSessionHttpServletResponse response = (WintSessionHttpServletResponse) filterContext.getAttribute(AttrKeys.NEW_RESPONSE);
        response.addWintCookie(cookie);
    }

    @Override
    public Set<String> getNames() {
        return exec(new Command<Set<String>>() {
            @Override
            public Set<String> doInExec(Jedis jedis) {
                return jedis.hkeys(sessionId);
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
