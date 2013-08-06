package wint.tools.similar.proccess;

import java.util.Map;

import wint.tools.util.CollectionUtil;

public class DefaultProcessContext implements ProcessContext {

	private Map<String, Object> attributes = CollectionUtil.newHashMap();
	
	private ContentTerm contentTerm;
	
	private String groupId;
	
	private boolean updateTermCount;
	
	public DefaultProcessContext(String id) {
		this.contentTerm = new ContentTerm(id);
	}
	
	public ContentTerm getContentTerm() {
		return contentTerm;
	}

	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public boolean isUpdateTermCount() {
		return updateTermCount;
	}

	public void setUpdateTermCount(boolean updateTermCount) {
		this.updateTermCount = updateTermCount;
	}

}
