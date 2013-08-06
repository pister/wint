package wint.tools.similar.content;

import wint.tools.util.CloseableIterator;


/**
 * 获取文档的资源内容
 * 2012-8-21 下午3:19:11
 */
public interface ContentResource {
	
	CloseableIterator<Content> getContents(String groupId);
	
}
