package wint.tools.similar.store.redis;

import wint.tools.redis.RedisClient;

public class RedisBase {
	
	protected RedisClient redisClient;
	
	protected int namespace;
	
	public RedisBase(RedisClient redisClient, int namespace) {
		super();
		this.redisClient = redisClient;
		this.namespace = namespace;
	}

	protected String makeString(String groupId, String term) {
		return groupId + "_" + term;
	}
	

}
