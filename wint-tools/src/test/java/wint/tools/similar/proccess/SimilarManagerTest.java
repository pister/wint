package wint.tools.similar.proccess;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import wint.tools.redis.DefaultRedisClient;
import wint.tools.similar.DefaultSimilarManager;
import wint.tools.similar.SimilarItem;
import wint.tools.similar.content.ContentResource;
import wint.tools.similar.content.FsContentResource;
import wint.tools.similar.store.ContentTermStore;
import wint.tools.similar.store.TermCountStore;
import wint.tools.similar.store.fs.FsContentTermStore;
import wint.tools.similar.store.redis.RedisContentTermStore;
import wint.tools.similar.store.redis.RedisTermCountStore;
import wint.tools.similar.store.sql.SqlContentTermStore;
import wint.tools.similar.store.sql.SqlTermCountStore;
import wint.tools.sql.SingleDataSource;

public class SimilarManagerTest extends TestCase {

	public void testNormal() {
		int countNameSpace = 103;
		
		DefaultRedisClient redisClient = new DefaultRedisClient();
		redisClient.setRedisHosts("127.0.0.1:6379");
		redisClient.init();
		
		ContentTermStore contentTermStore = new FsContentTermStore(new File("D:/terms"));
		
		TermCountStore termCountStore = new RedisTermCountStore(redisClient, countNameSpace);
		doSimilar(contentTermStore, termCountStore);
	}
	
	
	public void testRedis() {
		int contentNameSpace = 104;
		int countNameSpace = 105;
		
		DefaultRedisClient redisClient = new DefaultRedisClient();
		redisClient.setRedisHosts("127.0.0.1:6379");
		redisClient.init();
		
		ContentTermStore contentTermStore = new RedisContentTermStore(redisClient, contentNameSpace);
		TermCountStore termCountStore = new RedisTermCountStore(redisClient, countNameSpace);
		doSimilar(contentTermStore, termCountStore);
	}
	
	public void testMem() {
		ContentTermStore contentTermStore = new MemContentTermStore();
		TermCountStore termCountStore = new MemTermCountStore();
		doSimilar(contentTermStore, termCountStore);
	}
	
	public void testFile() {
		ContentTermStore contentTermStore = new FsContentTermStore(new File("D:/terms"));
		TermCountStore termCountStore = new MemTermCountStore();
		doSimilar(contentTermStore, termCountStore);
	}
	
	public void testSqlStore() {
		String url = "jdbc:mysql://127.0.0.1:3306/test1?useUnicode=true&amp;characterEncoding=utf8";
		String driverClass = "com.mysql.jdbc.Driver";
		String username = "test1";
		String password = "test1";
		SingleDataSource singleDataSource = new SingleDataSource(url, driverClass, username, password);
		try {
			singleDataSource.init();
			
			SqlContentTermStore contentTermStore = new SqlContentTermStore();
			contentTermStore.setDataSource(singleDataSource);
			SqlTermCountStore termCountStore = new SqlTermCountStore();
			termCountStore.setDataSource(singleDataSource);
			
			doSimilar(contentTermStore, termCountStore);
		} finally {
			singleDataSource.destroy();
		}
	}
	
	private void doSimilar(ContentTermStore contentTermStore, TermCountStore termCountStore) {
		String path = "D:/temp";
		ContentResource contentResource = new FsContentResource(path);
		DefaultSimilarManager similarManager = new DefaultSimilarManager(contentTermStore, termCountStore);
		String groupId = "testGroupId";
		similarManager.processResource(groupId, contentResource);
		
		String inputText = "计算机语言排行榜";
		
		List<SimilarItem> result = similarManager.findTopSimilars(inputText, groupId, 3);
		for (SimilarItem similarItem : result) {
			System.out.println(similarItem);
		}
	}
	
}
