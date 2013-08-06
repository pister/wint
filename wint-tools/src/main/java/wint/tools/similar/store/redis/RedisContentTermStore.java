package wint.tools.similar.store.redis;

import java.util.Set;

import wint.tools.redis.RedisClient;
import wint.tools.similar.proccess.ContentTerm;
import wint.tools.similar.store.ContentTermStore;
import wint.tools.util.CloseableIterator;

public class RedisContentTermStore extends RedisBase implements ContentTermStore {
	
	public RedisContentTermStore(RedisClient redisClient, int namespace) {
		super(redisClient, namespace);
	}

	public void save(String groupId, String id, ContentTerm contentTerm) {
		redisClient.hset(namespace, groupId, id, contentTerm);
	}

	public int getContentTermCount(String groupId) {
		Long value = redisClient.hlen(namespace, groupId);
		if (value == null) {
			return 0;
		}
		return value.intValue();
	}

	public CloseableIterator<ContentTerm> getContentTerms(String groupId) {
		Set<String> ids = redisClient.hkeys(namespace, groupId);
		return new FectchContentTermIterator(redisClient, namespace, groupId, ids);
	}

}
