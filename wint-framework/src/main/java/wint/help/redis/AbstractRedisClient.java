package wint.help.redis;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

/**
 * User: huangsongli
 * Date: 15/4/14
 * Time: 上午11:36
 */
public abstract class AbstractRedisClient<T> implements RedisClient {

    /**
     * 主机名 host1:port1:<name1>;host2:port2:<name2>;host3:port3:<name3>
     */
    private String serverAddress;
    private int maxTotal = 100;
    private int maxIdle = 10;
    private int minIdle = 1;
    private RedisTemplate redisTemplate;

    protected int timeout = 2000; // default 2000 ms

    protected int database = RedisUtil.DEFAULT_DATABASE;

    public void init() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMaxWaitMillis(30000);

        this.redisTemplate = new RedisTemplate(getPool(jedisPoolConfig, serverAddress));
    }

    protected abstract Pool<T> getPool(JedisPoolConfig jedisPoolConfig, String serverAddress);

    @Override
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public final void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public final void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public final void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase() {
        return database;
    }
}
