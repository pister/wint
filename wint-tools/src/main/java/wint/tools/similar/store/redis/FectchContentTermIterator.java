package wint.tools.similar.store.redis;

import java.util.Iterator;
import java.util.Set;

import wint.tools.redis.RedisClient;
import wint.tools.similar.proccess.ContentTerm;
import wint.tools.util.CloseableIterator;

public class FectchContentTermIterator implements CloseableIterator<ContentTerm> {

	private RedisClient redisClient;
	
	private int namespace;
	
	private String groupId;
	
	private Iterator<String> ids; 
	
	public FectchContentTermIterator(RedisClient redisClient, int namespace, String groupId, Set<String> ids) {
		super();
		this.redisClient = redisClient;
		this.namespace = namespace;
		this.groupId = groupId;
		if (ids != null) {
			this.ids = ids.iterator();
		}
	}

	public boolean hasNext() {
		return (ids != null) && ids.hasNext();
	}

	public ContentTerm next() {
		String id = ids.next();
		return redisClient.hget(namespace, groupId, id);
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public void close() {
		
	}

}
