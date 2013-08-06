package wint.tools.similar;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.tools.search.StopWords;
import wint.tools.similar.content.Content;
import wint.tools.similar.content.ContentResource;
import wint.tools.similar.content.StringContent;
import wint.tools.similar.proccess.ContentTerm;
import wint.tools.similar.proccess.DefaultProcessContext;
import wint.tools.similar.proccess.ProccessStep;
import wint.tools.similar.proccess.ProcessContext;
import wint.tools.similar.proccess.SimilarTerm;
import wint.tools.similar.proccess.steps.LuceneAnalyzeStep;
import wint.tools.similar.proccess.steps.TermFreqStep;
import wint.tools.similar.store.ContentTermStore;
import wint.tools.similar.store.TermCountStore;
import wint.tools.util.CloseableIterator;
import wint.tools.util.CollectionUtil;
import wint.tools.util.CountLimitTreeMap;

public class DefaultSimilarManager implements SimilarManager {

	private static final Logger log = LoggerFactory.getLogger(DefaultSimilarManager.class);
	
	private ContentTermStore contentTermStore;
	
	private TermCountStore termCountStore;
	
	private List<ProccessStep> proccessSteps = CollectionUtil.newArrayList();
	
	public DefaultSimilarManager(ContentTermStore contentTermStore, TermCountStore termCountStore) {
		this.contentTermStore = contentTermStore;
		this.termCountStore = termCountStore;
		init();
	}
	
	protected Analyzer getLuceneAnalyzer() {
	//	return new PaodingAnalyzer();
		return new StandardAnalyzer(Version.LUCENE_30, StopWords.getStopWords());
	}
	
	private void init() {
		proccessSteps.add(new LuceneAnalyzeStep(getLuceneAnalyzer()));
		proccessSteps.add(new TermFreqStep());
	}
	
	public void processResource(String groupId, ContentResource contentResource) {
		CloseableIterator<Content> contentIterator = null;
		try {
			long count = 0;
			contentIterator = contentResource.getContents(groupId);;
			while (contentIterator.hasNext()) {
				Content content = contentIterator.next();
				ContentTerm contentTerm = proccessContent(groupId, content, true);
				contentTermStore.save(groupId, contentTerm.getId(), contentTerm);
				++count;
				if (log.isDebugEnabled()) {
					if (count % 10 == 0) {
						log.debug("process resource finish:" + count);
					}
				}
				
				if (log.isWarnEnabled()) {
					if (count % 100 == 0) {
						log.warn("process resource finish:" + count);
					}
				}
			}
		} finally {
			contentIterator.close();
		}
		proccessTfIdfAll(groupId);
	}
	
	public List<SimilarItem> findTopSimilars(String inputText, String groupId, int count) {
		Content content = new StringContent("inputText", inputText);
		ContentTerm contentTerm = proccessContent(groupId, content, false);
		double itemSize = contentTermStore.getContentTermCount(groupId);
		itemSize += 1;
		proccessTfIdf(groupId, itemSize, contentTerm);
		
		long ct = 0;
		
		CountLimitTreeMap<Double, String> countLimitTreeMap = new CountLimitTreeMap<Double, String>(count);
		CloseableIterator<ContentTerm> contentTermIterator = null;
		try {
			contentTermIterator = contentTermStore.getContentTerms(groupId);
			while (contentTermIterator.hasNext()) {
				ContentTerm contentTerm2 = contentTermIterator.next();
				double value = computeSimilar(contentTerm, contentTerm2);
				countLimitTreeMap.put(value, contentTerm2.getId());
				
				++ct;
				
				if (log.isDebugEnabled()) {
					if (ct % 10 == 0) {
						log.debug("findTopSimilars scaning doc count:" + ct);
					}
				}
				
				if (log.isWarnEnabled()) {
					if (ct % 100 == 0) {
						log.warn("findTopSimilars scaning doc count:" + ct);
					}
				}
			}
		} finally {
			contentTermIterator.close();
		}
		
		List<SimilarItem> ret = CollectionUtil.newArrayList(countLimitTreeMap.size());
		for (Map.Entry<Double, String> entry : countLimitTreeMap.entrySet()) {
			ret.add(new SimilarItem(entry.getValue(), entry.getKey()));
		}
		
		Collections.reverse(ret);
		
		return ret;
	}
	
	private static double computeSimilar(ContentTerm i1, ContentTerm i2) {
		Map<String, SimilarTerm> t1 = i1.getSimilarTerms();
		Map<String, SimilarTerm> t2 = i2.getSimilarTerms();
		/*
		 * 
		 * (x1*x2+y1*y2)/(sqrt(x1*x1 + y1*y1) + sqrt(x2*x2 + y2*y2))
		 * 
		 */
		double fenzi = 0.0;  // 
		for (Map.Entry<String, SimilarTerm> entry : t1.entrySet())  {
			String term = entry.getKey();
			SimilarTerm similarTerm1 = entry.getValue();
			SimilarTerm similarTerm2 = t2.get(term);
			if (similarTerm2 == null) {
				continue;
			}
			double v1 = similarTerm1.getTfidf();
			double v2 = similarTerm2.getTfidf();
			fenzi += v1 * v2;
		}
		double fenmu = i1.getSqrtResult() * i2.getSqrtResult();
		if (isZero(fenmu)) {
			return 0.0;
		}
		return fenzi / fenmu;
	}
	
	static boolean isZero(double v) {
		if (Math.abs(v) < 0.000001) {
			return true;
		}
		return false;
	}
	
	protected ContentTerm proccessTfIdf(String groupId, double itemSize, ContentTerm contentTerm) {
		Map<String, SimilarTerm> similarTerms = contentTerm.getSimilarTerms();
		double sum = 0.0;
		for (Map.Entry<String, SimilarTerm> entry : similarTerms.entrySet()) {
			SimilarTerm similarTerm = entry.getValue();
			String term = entry.getKey();
			long count = this.termCountStore.getTermCount(groupId, term);
			if (count == 0) {
				continue;
			}
			double idf = Math.log(itemSize / count);
			double tfidf = idf * similarTerm.getTf();
			similarTerm.setIdf(idf);
			similarTerm.setTfidf(tfidf);
			
			double value = tfidf * tfidf;
			sum += value;
		}
		double sqrtValue = Math.sqrt(sum);
		contentTerm.setSqrtResult(sqrtValue);
		return contentTerm;
	}
	
	protected void proccessTfIdfAll(String groupId) {
		log.warn("proccessing TfIdf ...");
		double itemSize = contentTermStore.getContentTermCount(groupId);
		CloseableIterator<ContentTerm> contentTermIterator = null;
		try {
			long ct = 0;
			contentTermIterator = contentTermStore.getContentTerms(groupId);
			while (contentTermIterator.hasNext()) {
				ContentTerm contentTerm = contentTermIterator.next();
				proccessTfIdf(groupId, itemSize, contentTerm);
				contentTermStore.save(groupId, contentTerm.getId(), contentTerm);
				++ct;
				if (ct % 2000 == 0) {
					log.warn("TfIdf finish count: " + ct);
				}
			}
		} finally {
			contentTermIterator.close();
		}
		log.warn("proccess TfIdf finish.");
	}
	
	
	protected ContentTerm proccessContent(String groupId, Content content, boolean updateTermCount) {
		ProcessContext processContext = new DefaultProcessContext(content.getId());
		processContext.setUpdateTermCount(updateTermCount);
		processContext.setAttribute("termCountStore", termCountStore);
		processContext.setGroupId(groupId);
		for (ProccessStep proccessStep : proccessSteps) {
			proccessStep.execute(content, processContext);
		}
		return processContext.getContentTerm();
	}

}
