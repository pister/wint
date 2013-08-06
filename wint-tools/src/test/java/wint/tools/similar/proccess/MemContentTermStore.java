package wint.tools.similar.proccess;

import java.util.Map;

import wint.tools.similar.store.ContentTermStore;
import wint.tools.util.CloseableIterator;
import wint.tools.util.CloseableIteratorWrapper;
import wint.tools.util.CollectionUtil;

public class MemContentTermStore implements ContentTermStore {

	Map<String, Map<String, ContentTerm>> holder = CollectionUtil.newHashMap();
	
	public void save(String groupId, String id, ContentTerm contentTerm) {
		get(groupId).put(id, contentTerm);
	}
	
	protected Map<String, ContentTerm> get(String groupId) {
		Map<String, ContentTerm> ret = holder.get(groupId);
		if (ret != null) {
			return ret;
		}
		ret = CollectionUtil.newHashMap();
		holder.put(groupId, ret);
		return ret;
	}

	public int getContentTermCount(String groupId) {
		return get(groupId).size();
	}

	public CloseableIterator<ContentTerm> getContentTerms(String groupId) {
		return new CloseableIteratorWrapper<ContentTerm>(get(groupId).values().iterator());
	}

}
