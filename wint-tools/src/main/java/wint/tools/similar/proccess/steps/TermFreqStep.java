package wint.tools.similar.proccess.steps;

import java.util.Map;

import wint.tools.similar.content.Content;
import wint.tools.similar.proccess.ContentTerm;
import wint.tools.similar.proccess.ProccessStep;
import wint.tools.similar.proccess.ProcessContext;
import wint.tools.similar.proccess.SimilarTerm;
import wint.tools.similar.store.TermCountStore;

public class TermFreqStep implements ProccessStep {

	public void execute(Content content, ProcessContext processContext) {
		ContentTerm contentTerm = processContext.getContentTerm();
		Map<String, SimilarTerm> similarTerms = contentTerm.getSimilarTerms();
		String groupId = processContext.getGroupId();
		TermCountStore termCountStore = (TermCountStore)processContext.getAttribute("termCountStore");
		
		for (Map.Entry<String, SimilarTerm> entry : similarTerms.entrySet()) {
			String term = entry.getKey();
			if (processContext.isUpdateTermCount()) {
				termCountStore.addTermCount(groupId, term, 1);
			}
		}
		
		double doubleTotal = (double)contentTerm.getTermTotal();
		for (Map.Entry<String, SimilarTerm> entry : similarTerms.entrySet()) {
			SimilarTerm similarTerm = entry.getValue();
			double freq = similarTerm.getCount() / doubleTotal;
			similarTerm.setTf(freq);
		}
	}
	
}
