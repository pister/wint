package wint.tools.similar.proccess.steps;

import java.util.Iterator;
import java.util.Map;

import wint.tools.similar.content.Content;
import wint.tools.similar.proccess.ContentTerm;
import wint.tools.similar.proccess.ProccessStep;
import wint.tools.similar.proccess.ProcessContext;
import wint.tools.similar.proccess.SimilarTerm;
import wint.tools.similar.term.TermAnalyzer;
import wint.tools.util.CollectionUtil;

public abstract class AnalyzeTermStep implements ProccessStep {

	protected abstract TermAnalyzer getTermAnalyzer();
	
	private int maxTermLength = 20;
	
	public void execute(Content content, ProcessContext processContext) {
		TermAnalyzer termAnalyzer = getTermAnalyzer();
		Iterator<String> termIterator = termAnalyzer.analyze(content.getValue());
		
		int termTotal = 0;
		Map<String, SimilarTerm> similarTerms = CollectionUtil.newHashMap();
		
		while (termIterator.hasNext()) {
			String term = termIterator.next();
			if (term.length() > maxTermLength) {
				continue;
			}
			SimilarTerm similarTerm = similarTerms.get(term);
			if (similarTerm == null) {
				similarTerms.put(term, new SimilarTerm(term));
			} else {
				similarTerm.incrCount();
			}
			++termTotal;
		}
		
		ContentTerm contentTerm = processContext.getContentTerm();
		contentTerm.setTermTotal(termTotal);
		contentTerm.setSimilarTerms(similarTerms);
	}

}
