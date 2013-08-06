package wint.tools.similar.proccess;

import java.util.Map;

import wint.tools.similar.store.TermCountStore;
import wint.tools.util.CollectionUtil;

public class MemTermCountStore implements TermCountStore {

	Map<String, Map<String, Long>> holder = CollectionUtil.newHashMap();
	
	public void addTermCount(String groupId, String term, int count) {
		Map<String, Long> m = get(groupId);
		Long ct = m.get(term);
		if (ct == null) {
			m.put(term, 1L);
		} else {
			m.put(term, ct + count);
		}
	}

	protected Map<String, Long> get(String groupId) {
		Map<String, Long> ret = holder.get(groupId);
		if (ret != null) {
			return ret;
		}
		ret = CollectionUtil.newHashMap();
		holder.put(groupId, ret);
		return ret;
	}
	
	public long getTermCount(String groupId, String term) {
		Map<String, Long> m = get(groupId);
		Long ct = m.get(term);
		if (ct == null) {
			return 0L;
		}
		return ct;
	}
	
	public void printTermCounts(String groupId) {
		Map<String, Long> ret =get(groupId);
		for (Map.Entry<String, Long> entry : ret.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

}
