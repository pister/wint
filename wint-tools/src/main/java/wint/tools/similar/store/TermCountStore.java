package wint.tools.similar.store;

/**
 * 
 * 存放term的次数（汇总每个文档）
 * 2012-8-21 下午3:21:24
 */
public interface TermCountStore {
	
	void addTermCount(String groupId, String term, int count);
	
	long getTermCount(String groupId, String term);
	
}
