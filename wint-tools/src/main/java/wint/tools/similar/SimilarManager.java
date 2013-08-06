package wint.tools.similar;

import java.util.List;

import wint.tools.similar.content.ContentResource;


public interface SimilarManager {
	
	/**
	 * 处理资源
	 * @param groupId
	 * @param contentResource
	 */
	void processResource(String groupId, ContentResource contentResource);
	
	/**
	 * 找到最匹配的内容
	 * @param inputText
	 * @param groupId
	 * @param count
	 * @return
	 */
	List<SimilarItem> findTopSimilars(String inputText, String groupId, int count);
	
	
}
