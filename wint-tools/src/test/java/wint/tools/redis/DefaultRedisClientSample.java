package wint.tools.redis;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import junit.framework.TestCase;
import wint.tools.util.CollectionUtil;

public class DefaultRedisClientSample extends TestCase {

    public static void main(String[] args) {
        DefaultRedisClientSample defaultRedisClientTest = new DefaultRedisClientSample();
        defaultRedisClientTest.testInit();
    }

    public void testMultiGet() {
        DefaultRedisClient defaultRedisClient = new DefaultRedisClient();
        defaultRedisClient.setRedisHosts("127.0.0.1:6379");
        defaultRedisClient.init();
        int namespace = 1000;
        Map<String, String> values = CollectionUtil.newHashMap();
        values.put("key1", "value1");
        values.put("key2", "value2");
        values.put("key3", "value3");
        defaultRedisClient.mset(namespace, values);

        defaultRedisClient.set(namespace, "key6", "value6");
        {
            String value = defaultRedisClient.get(namespace, "key1");
            System.out.println(value);
        }
        {
            String value = defaultRedisClient.get(namespace, "key2");
            System.out.println(value);
        }
        {
            String value = defaultRedisClient.get(namespace, "key3");
            System.out.println(value);
        }
        List<String> vals = defaultRedisClient.mget(namespace, Arrays.asList("key2", "key3", "key4", "key6"));
        System.out.println(vals);
    }

    public void testCounting() {
        DefaultRedisClient defaultRedisClient = new DefaultRedisClient();
        defaultRedisClient.setRedisHosts("127.0.0.1:6379");
        defaultRedisClient.init();

        int namespace = 5555;
        String key = "name1";

        for (int i = 0; i < 10; ++i) {
            defaultRedisClient.incr(namespace, key);
        }
        long v = defaultRedisClient.getCounting(namespace, key);
        System.out.println(v);
    }

    public void testInit() {
        DefaultRedisClient defaultRedisClient = new DefaultRedisClient();
        defaultRedisClient.setRedisHosts("127.0.0.1:6379");
        defaultRedisClient.init();

        int count = 20;
        long loop = 50000;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        int namespace = 10;
        long ts = System.currentTimeMillis();
        for (int i = 0; i < count; ++i) {
            BenchThread bt = new BenchThread(countDownLatch, defaultRedisClient, namespace + i, "key" + i, loop);
            bt.start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long times = count * loop;
        long delta = System.currentTimeMillis() - ts;

        System.out.println("done! qps:" + (times * 1000.0 / delta));
    }

    static class BenchThread extends Thread {
        RedisClient redisClient;
        int namespace;
        String key;
        CountDownLatch countDownLatch;
        long loop;

        public BenchThread(CountDownLatch countDownLatch, RedisClient redisClient, int namespace, String key, long loop) {
            super();
            this.countDownLatch = countDownLatch;
            this.redisClient = redisClient;
            this.namespace = namespace;
            this.key = key;
            this.loop = loop;
        }

        public void run() {
            for (int i = 0; i < loop; ++i) {
                try {
                    redisClient.set(namespace, key, "key:" + key + ",val:" + i);
                    String value = redisClient.get(namespace, key);
                    if (i % 500 == 0) {
                        System.out.println("ns: " + namespace + ", val: " + value);
                    }
                    //	Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //	System.out.println("ns: " + namespace + ", val: " + value);
            }
            countDownLatch.countDown();
        }

    }

}
