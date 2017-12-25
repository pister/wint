package wint.help.redis;

/**
 * User: huangsongli
 * Date: 15/4/15
 * Time: 下午5:09
 */
public class RedisClientFactory {

    private static final String PROTOCOL_MARK = "://";
    private static final String SENTINEL = "sentinel";
    private static final String SHARD_REDIS = "shard-redis";
    private static final String REDIS = "redis";
    /**
     * sentinel://mymaster@192.168.1.226:26379;192.168.1.226:26379          =>  sentinel
     * shard-redis://192.168.1.226:6379:name-1;192.168.1.226:6379:name-2    => shard
     * redis://192.168.1.226:6379                                           => redis
     *
     *
     * 192.168.1.226:6379    => redis://192.168.1.226:6379
     * 192.168.1.226:6379;192.168.1.226:6379    => redis://192.168.1.226:6379
     * 192.168.1.226:6379:name-1;192.168.1.226:6379:name-2 => shard-redis://192.168.1.226:6379:name-1;192.168.1.226:6379:name-2
     * mymaster@192.168.1.226:26379 => sentinel://mymaster@192.168.1.226:26379
     * mymaster@192.168.1.226:26379;192.168.1.226:26379 => sentinel://mymaster@192.168.1.226:26379;192.168.1.226:26379
     */
    private String serverAddress;
    private int maxTotal = 100;
    private int maxIdle = 10;
    private int minIdle = 1;

    private int timeout = 2000;

    public RedisClient getRedisClient() {
        int protocolPos = serverAddress.indexOf(PROTOCOL_MARK);
        String protocolName;
        String hosts;
        if (protocolPos > 0) {
            protocolName = serverAddress.substring(0, protocolPos);
            hosts = serverAddress.substring(protocolPos + PROTOCOL_MARK.length());
        } else {
            hosts = serverAddress;
            if (serverAddress.contains("@")) {
                protocolName = SENTINEL;
            } else if (serverAddress.split(";")[0].split(":").length > 2) {
                protocolName = SHARD_REDIS;
            } else {
                protocolName = REDIS;
            }
        }
        if (REDIS.equals(protocolName)) {
            AbstractRedisClient redisClient = new DefaultRedisClient();
            setProperties(redisClient, hosts);
            redisClient.init();
            return redisClient;
        }
        if (SHARD_REDIS.equals(protocolName)) {
            AbstractRedisClient redisClient = new ShardedRedisClient();
            setProperties(redisClient, hosts);
            redisClient.init();
            return redisClient;
        }
        if (SENTINEL.equals(protocolName)) {
            AbstractRedisClient redisClient = new SentinelRedisClient();
            setProperties(redisClient, hosts);
            redisClient.init();
            return redisClient;
        }
        throw new RuntimeException("not support protocol:" + protocolName);
    }

    private void setProperties(AbstractRedisClient redisClient, String hosts) {
        redisClient.setServerAddress(hosts);
        redisClient.setMinIdle(minIdle);
        redisClient.setMaxIdle(maxIdle);
        redisClient.setMaxTotal(maxTotal);
        redisClient.setTimeout(timeout);
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
