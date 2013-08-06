package wint.tools.cache;

import junit.framework.TestCase;
import wint.tools.redis.DefaultRedisClient;
import wint.tools.util.CollectionUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pister
 * Date: 13-5-4
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public class RedisCacheManagerTest extends TestCase {

    public void testForHandle() {
        DefaultRedisClient defaultRedisClient = new DefaultRedisClient();
        defaultRedisClient.setRedisHosts("127.0.0.1:6379");
        defaultRedisClient.init();
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisClient(defaultRedisClient);
        int namespace = 1001;
        int expire = 100 * 60;

        {
            String value = cacheManager.getForHandle(namespace, "key11", new SingleKeyHandler<String, String>() {
                public String fetchItem(String key) throws Exception {
                    System.out.println("fetchItem");
                    return "value for key: " + key;
                }
            }, expire);
            System.out.println(value);
        }


        {
            String value = cacheManager.getForHandle(namespace, "key11", new SingleKeyHandler<String, String>() {
                public String fetchItem(String key) throws Exception {
                    System.out.println("fetchItem");
                    return "value for key: " + key;
                }
            }, expire);
            System.out.println(value);
        }

        {
            List<String> keys = Arrays.asList("key1", "key2", "key11", "key20", "key21");
            Map<String, String> result = cacheManager.mgetForHandle(namespace, keys, new MultiKeysHandler<String, String>() {
                public Map<String, String> fetchMultiItems(Collection<String> keys) throws Exception {
                    Map<String, String> ret = CollectionUtil.newHashMap();
                    for (String key : keys) {
                        System.out.println("fetchItem");
                        ret.put(key, "mvalue:" + key);
                    }
                    return ret;
                }
            }, expire);
            System.out.println(result);
        }

        {
            List<String> keys = Arrays.asList("key1", "key25" ,"key2", "key11", "key20", "key21", "key22", "key24");
            Map<String, String> result = cacheManager.mgetForHandle(namespace, keys, new MultiKeysHandler<String, String>() {
                public Map<String, String> fetchMultiItems(Collection<String> keys) throws Exception {
                    Map<String, String> ret = CollectionUtil.newHashMap();
                    for (String key : keys) {
                        System.out.println("fetchItem");
                        ret.put(key, "mvalue:" + key);
                    }
                    return ret;
                }
            }, expire);
            System.out.println(result);
        }
    }

    public void testMultiGet() {
        DefaultRedisClient defaultRedisClient = new DefaultRedisClient();
        defaultRedisClient.setRedisHosts("127.0.0.1:6379");
        defaultRedisClient.init();
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisClient(defaultRedisClient);
        int namespace = 1001;
        int expire = 100 * 60;
        cacheManager.set(namespace, "key1", "value1", expire);
        {
            Entry<String> value = cacheManager.get(namespace, "key1");
            System.out.println(value);
            if (value != null) {
                System.out.println(value.getObject());
            }
        }

        {
            Entry<String> value = cacheManager.get(namespace, "key2");
            System.out.println(value);
            if (value != null) {
                System.out.println(value.getObject());
            }
        }

        Map<String, String> values = CollectionUtil.newHashMap();
        values.put("key2", "value2");
        values.put("key3", "value3");
        values.put("key4", "value4");
        cacheManager.mset(namespace, values, expire);

        {
            Map<String, Entry<String>> result = cacheManager.mget(namespace, Arrays.asList("key2", "key3", "key4"));
            System.out.println(result);
        }
        {
            Entry<String> value = cacheManager.get(namespace, "key2");
            System.out.println(value);
            if (value != null) {
                System.out.println(value.getObject());
            }
        }
        {
            Entry<String> value = cacheManager.get(namespace, "key3");
            System.out.println(value);
            if (value != null) {
                System.out.println(value.getObject());
            }
        }
        {
            Entry<String> value = cacheManager.get(namespace, "key4");
            System.out.println(value);
            if (value != null) {
                System.out.println(value.getObject());
            }
        }
    }

}
