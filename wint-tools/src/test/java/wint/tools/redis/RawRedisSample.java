package wint.tools.redis;

import java.util.concurrent.CountDownLatch;

import redis.clients.jedis.BinaryJedis;

public class RawRedisSample {
	
	public static void main(String[] args) {
		RawRedisSample rawRedisTest = new RawRedisSample();
		rawRedisTest.testRawRedis();
	}
	
	public void testRawRedis() {
		BinaryJedis binaryJedis = new BinaryJedis("127.0.0.1", 6379);
		int count = 1;
		long loop = 2000;
		CountDownLatch countDownLatch = new CountDownLatch(count);
		int namespace = 10;
		long ts = System.currentTimeMillis();
		for (int i = 0; i < count; ++i) {
			BenchThread bt = new BenchThread(countDownLatch, binaryJedis, namespace + i, "key"+i, loop);
			bt.start();
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long times = count * loop;
		long delta = System.currentTimeMillis() - ts;
		
		System.out.println("done! qps:" + (times *1000.0 / delta));
		
	}

	static class BenchThread extends Thread {
		BinaryJedis binaryJedis;
		int namespace;
		String key;
		CountDownLatch countDownLatch;
		long loop;
		public BenchThread(CountDownLatch countDownLatch, BinaryJedis redisClient, int namespace, String key, long loop) {
			super();
			this.countDownLatch = countDownLatch;
			this.binaryJedis = redisClient;
			this.namespace = namespace;
			this.key = key;
			this.loop = loop;
		}
		public void run() {
			for (int i = 0; i < loop; ++i) {
				try {
					byte[] bytes = key.getBytes();
					binaryJedis.set(bytes, ("key:" + key + ",val:" + i).getBytes());
					binaryJedis.expire(bytes, 60);
					String value = new String(binaryJedis.get(bytes));
					System.out.println("ns: " + namespace + ", val: " + value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			countDownLatch.countDown();
		}
		
	}
	
}
