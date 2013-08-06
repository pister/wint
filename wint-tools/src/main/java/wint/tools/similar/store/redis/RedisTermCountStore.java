package wint.tools.similar.store.redis;

import wint.tools.redis.RedisClient;
import wint.tools.similar.store.TermCountStore;

public class RedisTermCountStore extends RedisBase implements TermCountStore {
	
	public RedisTermCountStore(RedisClient redisClient, int namespace) {
		super(redisClient, namespace);
	}

	public void addTermCount(String groupId, String term, int count) {
		String key = makeString(groupId, term);
		redisClient.incrBy(namespace, key, count);
	}

	public long getTermCount(String groupId, String term) {
		String key = makeString(groupId, term);
		return redisClient.getCounting(namespace, key);
	}

}
