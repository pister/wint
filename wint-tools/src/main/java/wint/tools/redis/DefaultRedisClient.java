package wint.tools.redis;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import wint.tools.serialize.SerializeService;
import wint.tools.serialize.WintSerializeService;
import wint.tools.util.CollectionUtil;

public class DefaultRedisClient implements RedisClient {

    private static final Logger log = LoggerFactory.getLogger(DefaultRedisClient.class);

    private ExecutorService notifyExecutorService = Executors.newSingleThreadExecutor();

    private SerializeService serializeService = new WintSerializeService();

    private int timeout = 1000;

    private List<RedisHost> redisHosts = CollectionUtil.newArrayList();

    private Random random = new Random(System.currentTimeMillis());

    private AtomicBoolean inited = new AtomicBoolean(false);

    private int poolSize = 5;

    private LinkedBlockingQueue<Jedis> jedisPool;

    private AtomicBoolean closed = new AtomicBoolean(false);

    public DefaultRedisClient() {
    }

    /**
     * hostname1:port1,hostname2:port2,hostname2:port2
     * or
     * hostname1:port1;hostname2:port2;hostname2:port2
     *
     * @param hosts
     */
    public void setRedisHosts(String hosts) {
        if (hosts == null) {
            throw new RuntimeException("please use setRedisHosts to config redis hosts");
        }
        String[] parts = hosts.split("[,;]");
        if (parts == null || parts.length == 0) {
            throw new RuntimeException("please use setRedisHosts to config redis hosts");
        }
        List<RedisHost> newRedisHosts = CollectionUtil.newArrayList();
        for (String part : parts) {
            String[] hostStringArray = part.split(":");
            if (hostStringArray.length < 2) {
                throw new RuntimeException("redis host format must be like hostname:port ");
            }
            String hostname = hostStringArray[0];
            int port = Integer.parseInt(hostStringArray[1]);
            RedisHost redisHost = new RedisHost(hostname, port);
            newRedisHosts.add(redisHost);
        }
        this.redisHosts = newRedisHosts;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public void init() {
        if (!inited.compareAndSet(false, true)) {
            return;
        }
        jedisPool = new LinkedBlockingQueue<Jedis>(poolSize);

        for (int i = 0; i < poolSize; ++i) {
            Jedis jedis = connectRedis();
            collectJedis(jedis);
        }

    }

    private RedisHost randomHost() {
        if (CollectionUtil.isEmpty(redisHosts)) {
            throw new RuntimeException("no redis hosts config!");
        }
        int index = random.nextInt(redisHosts.size());
        return redisHosts.get(index);
    }

    private void JedisdisconnectRedis(Jedis jedis) {
        if (jedis == null) {
            return;
        }
        try {
            jedis.disconnect();
        } catch (Exception e) {
            // ignore;
        }
    }

    private Jedis connectRedis() {
        RedisHost redisHost = randomHost();
        try {
            Jedis ret = new Jedis(redisHost.getHostname(), redisHost.getPort(), timeout);
            ret.ping();
            return ret;
        } catch (Exception e) {
            log.warn("redis concect failed on " + redisHost + ".", e.getMessage());
            return null;
        }
    }

    protected Jedis getJedis() {
        if (closed.get()) {
            throw new RuntimeException("redis client has been closed.");
        }
        try {
            return jedisPool.poll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void collectJedis(Jedis jedis) {
        if (jedis != null) {
            if (!jedisPool.offer(jedis)) {
                jedis.disconnect();
            }
        }
    }

    protected <K> byte[] makeKey(int namespace, K key) {
        try {
            return (namespace + "_" + key).getBytes("utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <K> byte[][] makeKeys(int namespace, Collection<K> keys) {
        if (keys == null || keys.size() == 0) {
            return new byte[0][];
        }
        byte[][] targetKeys = new byte[keys.size()][];
        int i = 0;
        for (K key : keys) {
            byte[] targetKey = makeKey(namespace, key);
            targetKeys[i++] = targetKey;
        }
        return targetKeys;
    }

    protected <HashKey> byte[] encodeHashKey(HashKey hashKey) {
        return serializeService.serialize(hashKey);
    }

    protected Object decodeHashKey(byte[] rawKey) {
        return serializeService.deserialize(rawKey);
    }

    public static interface ExecuteHandle {

        Object forExecute(Jedis jedis);

    }

    protected Object execute(ExecuteHandle executeHandle) {
        Jedis binaryJedis = getJedis();
        if (binaryJedis == null) {
            notifyConnectRedis();
            return null;
        }
        try {
            return executeHandle.forExecute(binaryJedis);
        } catch (JedisConnectionException e) {
            log.warn("execute redis failed, retrying again.");
            binaryJedis = null;
            notifyConnectRedis();
            return null;
        } finally {
            collectJedis(binaryJedis);
        }
    }

    private AtomicBoolean connecting = new AtomicBoolean(false);

    private void notifyConnectRedis() {
        if (!connecting.compareAndSet(false, true)) {
            return;
        }

        Runnable t = new Runnable() {
            public void run() {
                try {
                    Jedis binaryJedis = connectRedis();
                    collectJedis(binaryJedis);
                } finally {
                    connecting.set(false);
                }
            }
        };
        notifyExecutorService.submit(t);
    }

    protected <HashKey> byte[][] encodeHashKeys(HashKey... hashKeys) {
        if (hashKeys == null || hashKeys.length == 0) {
            return new byte[0][];
        }
        List<byte[]> targetKeys = new ArrayList<byte[]>(hashKeys.length);
        for (HashKey key : hashKeys) {
            targetKeys.add(encodeHashKey(key));
        }
        return targetKeys.toArray(new byte[0][]);
    }

    public <K> void delete(final int namespace, final K... keys) {
        execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                jedis.del(makeKeys(namespace, Arrays.asList(keys)));
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <K, V> V get(final int namespace, final K key) {
        return (V)execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] data = jedis.get(targetKey);
                if (data == null) {
                    return null;
                }
                return (V) serializeService.deserialize(data);
            }
        });
    }

/*    private void traceKey(String msg, byte[] key) {
        StringBuilder sb = new StringBuilder();
        for (byte b : key) {
            sb.append(Integer.toHexString(b));
        }
        String m = sb.toString();
        System.out.println(msg + ": " + m);
    }*/

    /**
     * 仅仅用于计数器
     *
     * @param namespace
     * @param key
     * @return
     */
    private <K> String getCountingString(final int namespace, final K key) {
        return (String) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] data = jedis.get(targetKey);
                if (data == null) {
                    return "0";
                }
                return new String(data);
            }
        });
    }

    /**
     * 仅仅用于计数器
     *
     * @param namespace
     * @param key
     * @return
     */
    public <K> long getCounting(final int namespace, final K key) {
        try {
            return Long.parseLong(getCountingString(namespace, key));
        } catch (Exception e) {
            return 0L;
        }
    }

    public <K, HashKey> long hgetCounting(int namespace, K key, HashKey hashKey) {
        try {
            return Long.parseLong(hgetCountingString(namespace, key, hashKey));
        } catch (Exception e) {
            return 0L;
        }
    }

    private <K, HashKey> String hgetCountingString(final int namespace, final K key, final HashKey hashKey) {
        return (String) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] targetHashKey = encodeHashKey(hashKey);
                byte[] data = jedis.hget(targetKey, targetHashKey);
                if (data == null) {
                    return "0";
                }
                return new String(data);
            }
        });
    }


    /**
     * FIXME 这个方法有问题，需要慎用
     */
    @SuppressWarnings("unchecked")
    public <K, V> List<V> mget(final int namespace, final Collection<K> keys) {
        return (List<V>) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                List<byte[]> result = jedis.mget(makeKeys(namespace, keys));
                if (result == null) {
                    return null;
                }
                List<V> ret = CollectionUtil.newArrayList(result.size());
                for (byte[] data : result) {
                    if (data == null) {
                        ret.add(null);
                    } else {
                        ret.add((V) serializeService.deserialize(data));
                    }
                }
                return ret;
            }
        });
    }

    /**
     * @param namespace
     * @param m
     * @param <K>
     * @param <V>
     */
    public <K, V> void mset(final int namespace, final Map<K, V> m) {
        execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                if (CollectionUtil.isEmpty(m)) {
                    return null;
                }
                List<byte[]> keyvalues = CollectionUtil.newArrayList(m.size() * 2);
                for (Map.Entry<K, V> entry : m.entrySet()) {
                    byte[] targetKey = makeKey(namespace, entry.getKey());
                    byte[] data = serializeService.serialize(entry.getValue());

                    keyvalues.add(targetKey);
                    keyvalues.add(data);
                }
                jedis.mset(keyvalues.toArray(new byte[0][]));
                return null;
            }
        });
    }

    public <K, V> void set(final int namespace, final K key, final V value) {
        execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] data = serializeService.serialize(value);
                jedis.set(targetKey, data);
                return null;
            }
        });
    }


    @SuppressWarnings("unchecked")
    public <K, V> V getSet(final int namespace, final K key, final V newValue) {
        return (V) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] data = serializeService.serialize(newValue);
                byte[] ret = jedis.getSet(targetKey, data);
                if (ret == null) {
                    return null;
                }
                return (V) serializeService.deserialize(ret);
            }
        });
    }

    public <K> Long decr(final int namespace, final K key) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                return jedis.decr(targetKey);
            }
        });
    }

    public <K> Long decrBy(final int namespace, final K key, final long integer) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                return jedis.decrBy(targetKey, integer);
            }
        });
    }

    public <K> void expire(final int namespace, final K key, final int seconds) {
        execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                jedis.expire(targetKey, seconds);
                return null;
            }
        });
    }

    public <K> Long incr(final int namespace, final K key) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                return jedis.incr(targetKey);
            }
        });
    }

    public <K> Long incrBy(final int namespace, final K key, final long integer) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                return jedis.incrBy(targetKey, integer);
            }
        });
    }

    public <K> Boolean exists(final int namespace, final K key) {
        return (Boolean) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                return jedis.exists(targetKey);
            }
        });
    }

    public <K, V> Long append(final int namespace, final K key, final V value) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] data = serializeService.serialize(value);
                return jedis.append(targetKey, data);
            }
        });
    }

    public <K, HashKey> Long hdel(final int namespace, final K key, final HashKey... hashKeys) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[][] targetHashKeys = encodeHashKeys(hashKeys);
                return jedis.hdel(targetKey, targetHashKeys);
            }
        });
    }

    public <K, HashKey> Boolean hexists(final int namespace, final K key, final HashKey hashKey) {
        return (Boolean) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] targetHashKey = encodeHashKey(hashKey);
                return jedis.hexists(targetKey, targetHashKey);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <K, HashKey, HashValue> HashValue hget(final int namespace, final K key, final HashKey hashKey) {
        return (HashValue) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] targetHashKey = encodeHashKey(hashKey);
                byte[] data = jedis.hget(targetKey, targetHashKey);
                return (HashValue) serializeService.deserialize(data);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <K, HashKey, HashValue> Map<HashKey, HashValue> hgetAll(final int namespace, final K key) {
        return (Map<HashKey, HashValue>) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                Map<byte[], byte[]> result = jedis.hgetAll(targetKey);
                if (result == null) {
                    return null;
                }
                Map<HashKey, HashValue> ret = CollectionUtil.newHashMap();
                for (Map.Entry<byte[], byte[]> entry : result.entrySet()) {
                    byte[] rawKey = entry.getKey();
                    byte[] rawValue = entry.getValue();
                    HashKey hk = (HashKey) decodeHashKey(rawKey);
                    HashValue hv = (HashValue) serializeService.deserialize(rawValue);
                    ret.put(hk, hv);
                }
                return ret;
            }
        });
    }

    public <K, HashKey> Long hincrBy(final int namespace, final K key, final HashKey hashKey, final long value) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                return jedis.hincrBy(targetKey, encodeHashKey(hashKey), value);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <K, HashKey> Set<HashKey> hkeys(final int namespace, final K key) {
        return (Set<HashKey>) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                Set<byte[]> keys = jedis.hkeys(targetKey);
                if (keys == null) {
                    return null;
                }
                Set<HashKey> ret = new HashSet<HashKey>(keys.size());
                for (byte[] rawKey : keys) {
                    HashKey hashKey = (HashKey) decodeHashKey(rawKey);
                    ret.add(hashKey);
                }
                return ret;
            }
        });
    }

    public <K, HashKey> Long hlen(final int namespace, final K key) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                return jedis.hlen(targetKey);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <K, HashKey, HashValue> List<HashValue> hmget(final int namespace, final K key, final HashKey... hashKeys) {
        return (List<HashValue>) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[][] fields = encodeHashKeys(hashKeys);
                List<byte[]> values = jedis.hmget(targetKey, fields);
                if (values == null) {
                    return null;
                }
                List<HashValue> ret = CollectionUtil.newArrayList(values.size());
                for (byte[] data : values) {
                    ret.add((HashValue) serializeService.deserialize(data));
                }
                return ret;
            }
        });
    }

    public <K, HashKey, HashValue> String hmset(final int namespace, final K key, final Map<HashKey, HashValue> hash) {
        return (String) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                Map<byte[], byte[]> m = CollectionUtil.newHashMap();
                if (hash != null) {
                    for (Map.Entry<HashKey, HashValue> entry : hash.entrySet()) {
                        byte[] hkey = encodeHashKey(entry.getKey());
                        byte[] hvalue = serializeService.serialize(entry.getValue());
                        m.put(hkey, hvalue);
                    }
                }
                return jedis.hmset(targetKey, m);
            }
        });
    }

    public <K, HashKey, HashValue> Long hset(final int namespace, final K key, final HashKey hashKey, final HashValue hashValue) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] hkey = encodeHashKey(hashKey);
                byte[] hvalue = serializeService.serialize(hashValue);
                return jedis.hset(targetKey, hkey, hvalue);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <K, HashValue> List<HashValue> hvals(final int namespace, final K key) {
        return (List<HashValue>) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                List<byte[]> values = jedis.hvals(targetKey);
                if (values == null) {
                    return null;
                }
                List<HashValue> ret = CollectionUtil.newArrayList(values.size());
                for (byte[] v : values) {
                    ret.add((HashValue) serializeService.deserialize(v));
                }
                return ret;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <K, V> V lindex(final int namespace, final K key, final int index) {
        return (V) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] data = jedis.lindex(targetKey, index);
                return (V) serializeService.deserialize(data);
            }
        });
    }

    public <K> Long llen(final int namespace, final K key) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                return jedis.llen(targetKey);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <K, V> V lpop(final int namespace, final K key) {
        return (V) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] data = jedis.lpop(targetKey);
                if (data == null) {
                    return null;
                }
                return (V) serializeService.deserialize(data);
            }
        });
    }

    public <K, V> Long lpush(final int namespace, final K key, final V... strings) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                List<byte[]> values = CollectionUtil.newArrayList(strings.length);
                for (V v : strings) {
                    byte[] b = serializeService.serialize(v);
                    values.add(b);
                }
                return jedis.lpush(targetKey, values.toArray(new byte[0][]));
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <K, V> List<V> lrange(final int namespace, final K key, final int start, final int end) {
        return (List<V>) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                List<byte[]> values = jedis.lrange(targetKey, start, end);
                if (values == null) {
                    return null;
                }
                List<V> ret = CollectionUtil.newArrayList(values.size());
                for (byte[] b : values) {
                    ret.add((V) serializeService.serialize(b));
                }
                return ret;
            }
        });
    }

    public <K, V> Long lrem(final int namespace, final K key, final int count, final V value) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] b = serializeService.serialize(value);
                return jedis.lrem(targetKey, count, b);
            }
        });
    }

    public <K, V> String lset(final int namespace, final K key, final int index, final V value) {
        return (String) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] b = serializeService.serialize(value);
                return jedis.lset(targetKey, index, b);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <K, V> V rpop(final int namespace, final K key) {
        return (V) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] b = jedis.rpop(targetKey);
                return (V) serializeService.deserialize(b);
            }
        });
    }

    public <K, V> Long rpush(final int namespace, final K key, final V... strings) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                List<byte[]> bytes = CollectionUtil.newArrayList(strings.length);
                for (V v : strings) {
                    bytes.add(serializeService.serialize(v));
                }
                return jedis.rpush(targetKey, bytes.toArray(new byte[0][]));
            }
        });
    }

    public <K, V> Long sadd(final int namespace, final K key, final V... members) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                List<byte[]> bytes = CollectionUtil.newArrayList(members.length);
                for (V v : members) {
                    bytes.add(serializeService.serialize(v));
                }
                return jedis.sadd(targetKey, bytes.toArray(new byte[0][]));
            }
        });
    }

    public <K> Long scard(final int namespace, final K key) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                return jedis.scard(targetKey);
            }
        });
    }

    public <K, V> Boolean sismember(final int namespace, final K key, final V member) {
        return (Boolean) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                byte[] b = serializeService.serialize(member);
                return jedis.sismember(targetKey, b);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <K, V> Set<V> smembers(final int namespace, final K key) {
        return (Set<V>) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                Set<byte[]> bytes = jedis.smembers(targetKey);
                if (bytes == null) {
                    return null;
                }
                Set<V> ret = new HashSet<V>(bytes.size());
                for (byte[] b : bytes) {
                    ret.add((V) serializeService.deserialize(b));
                }
                return ret;
            }
        });
    }

    public <K, V> Long srem(final int namespace, final K key, final V... members) {
        return (Long) execute(new ExecuteHandle() {
            public Object forExecute(Jedis jedis) {
                byte[] targetKey = makeKey(namespace, key);
                List<byte[]> bytes = CollectionUtil.newArrayList(members.length);
                for (V v : members) {
                    bytes.add(serializeService.serialize(v));
                }
                return jedis.srem(targetKey, bytes.toArray(new byte[0][]));
            }
        });
    }

    public void close() {
        if (jedisPool == null) {
            return;
        }
        this.closed.set(true);
        List<Jedis> jedises = new ArrayList<Jedis>(jedisPool);
        for (Jedis binaryJedis : jedises) {
            try {
                binaryJedis.disconnect();
            } catch (Exception e) {
                // ignore
            }
        }
        jedisPool = null;
    }

}
