package wint.tools.similar.proccess;

public interface ProcessContext {
	
	ContentTerm getContentTerm();
	
	String getGroupId();
	
	void setGroupId(String groupId);
	
	void setAttribute(String name, Object value);
	
	Object getAttribute(String name);
	
	boolean isUpdateTermCount();
	
	void setUpdateTermCount(boolean updateTermCount);
	
}