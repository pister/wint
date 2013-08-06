package wint.tools.similar.store;

import wint.tools.similar.proccess.ContentTerm;
import wint.tools.util.CloseableIterator;

/**
 * 存放处理过（中）的每个文档
 * 2012-8-21 下午3:19:32
 */
public interface ContentTermStore {
	
	/**
	 * @param groupId
	 * @param contentTerm 根据contentTerm的id来判断，如果存在，则更新，否则新建
	 */
	void save(String groupId, String id, ContentTerm contentTerm);
	
	int getContentTermCount(String groupId);
	
	CloseableIterator<ContentTerm> getContentTerms(String groupId);
	

}
