package wint.tools.similar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import wint.tools.ikanalyzer.lucene.IKAnalyzer;

public class SimilarProccessor {

	private String targetDir;
	
	private List<ResultItem> resultItems;
	
	private Analyzer analyzer;
	
	private Map<String, Integer> globalTermCount;
	
	private Map<String, Integer> globalTermFreqs;
	
	public void init() {
		analyzer = new IKAnalyzer();
		resultItems = new ArrayList<ResultItem>();
		globalTermCount = new HashMap<String, Integer>();
		globalTermFreqs = new HashMap<String, Integer>();
	}
	
	
	public String getTargetDir() {
		return targetDir;
	}


	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}


	public static void main(String[] args) throws IOException {
		SimilarProccessor similarProccessor = new SimilarProccessor();
		similarProccessor.setTargetDir("d:/temp");
		similarProccessor.init();
		similarProccessor.proccess();
	}
	
	public void proccess() throws IOException {
		//SimilarResult ret = new SimilarResult();
		analyze();
		printTerms();
		
		Map<Double, Files> sortedSimilars = new TreeMap<Double, Files>();
		for (int i = 0, len = resultItems.size(); i < len; ++i) {
			ResultItem i1 = resultItems.get(i);
			for (int j = i + 1; j < len; ++j) {
				ResultItem i2 = resultItems.get(j);
				double similar = computeSimilar(i1, i2);
				Files files = new Files();
				files.v1 = i1.getFile().getName();
				files.v2 = i2.getFile().getName();
				sortedSimilars.put(similar, files);
				
			}
		}
		for (Map.Entry<Double, Files> entry : sortedSimilars.entrySet()) {
			double similar = entry.getKey();
			Files files = entry.getValue();
			System.out.println("similar:" + similar + " =======================");
			System.out.println(files.v1);
			System.out.println(files.v2);
			System.out.println("==========================================");
		}
	}
	
	static class Files {
		String v1;
		String v2;
	}
	
	double computeSimilar(ResultItem i1, ResultItem i2) {
		Map<String, SimilarTermAA> t1 = i1.getSimilarTerms();
		Map<String, SimilarTermAA> t2 = i2.getSimilarTerms();
		/*
		 * 
		 * (x1*x2+y1*y2)/(sqrt(x1*x1 + y1*y1) + sqrt(x2*x2 + y2*y2))
		 * 
		 */
		double fenzi = 0.0;  // 
		for (Map.Entry<String, SimilarTermAA> entry : t1.entrySet())  {
			String term = entry.getKey();
			SimilarTermAA similarTerm1 = entry.getValue();
			SimilarTermAA similarTerm2 = t2.get(term);
			if (similarTerm2 == null) {
				continue;
			}
			double v1 = similarTerm1.getTfidf();
			double v2 = similarTerm2.getTfidf();
			fenzi += v1 * v2;
			
		}
		double fenmu = i1.getSqrtResult() * i2.getSqrtResult();
		return fenzi / fenmu;
		
	}
	
	void printTerms() {
		for (ResultItem resultItem : resultItems) {
			System.out.println("===file:" + resultItem.getFile() + "============");
			Map<String, SimilarTermAA> similarTerms = resultItem.getSimilarTerms();
			
			for (Map.Entry<String, SimilarTermAA> entry : similarTerms.entrySet()) {
				SimilarTermAA similarTerm = entry.getValue();
				System.out.println(similarTerm);
			}
			System.out.println("====================================================================");
		}
	}
	
	private void analyze() throws IOException {
		File file = new File(targetDir);
		File[] textFiles = file.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});
		for (File f : textFiles) {
			termFreq(f);
		}
		
		idf();
	}
	
	private void termFreq(File file) throws IOException {
		System.out.println("analyzing " + file + " ...");
		Reader reader = new InputStreamReader(new FileInputStream(file), "gbk");
		
		TokenStream tokenStream = analyzer.tokenStream("content", reader);
		TermAttribute termAttribute = tokenStream.addAttribute(TermAttribute.class);
		Map<String, SimilarTermAA> similarTerms = new HashMap<String, SimilarTermAA>();
		ResultItem resultItem = new ResultItem();
		int total = 0;
		while (tokenStream.incrementToken()) {
			String term = termAttribute.term();
			SimilarTermAA similarTerm = similarTerms.get(term);
			if (similarTerm == null) {
				similarTerms.put(term, new SimilarTermAA(term));
			} else {
				similarTerm.incrCount();
			}
			
			Integer globalCount = globalTermCount.get(term);
			if (globalCount == null) {
				globalTermCount.put(term, 1);
			} else {
				globalTermCount.put(term, globalCount + 1);
			}
			
			++total;
		}
		for (Map.Entry<String, SimilarTermAA> entry : similarTerms.entrySet()) {
			String term = entry.getKey();
			Integer ct = globalTermFreqs.get(term);
			if (ct == null) {
				globalTermFreqs.put(term, 1);
			} else {
				globalTermFreqs.put(term, 1 + ct);
			}
		}
		// globalTermFreqs
		
		reader.close();
		
		resultItem.setFile(file);
		resultItem.setTermTotal(total);
		resultItem.setSimilarTerms(similarTerms);
		
		if (total == 0) {
			throw new RuntimeException("no term!");
		}
		
		double doubleTotal = (double)total;
		for (Map.Entry<String, SimilarTermAA> entry : similarTerms.entrySet()) {
			SimilarTermAA similarTerm = entry.getValue();
			double freq = similarTerm.getCount() / doubleTotal;
			similarTerm.setTf(freq);
		}
		
		resultItems.add(resultItem);
	}
	
	private void idf() {
		double itemSize = resultItems.size() ;
		for (ResultItem resultItem : resultItems) {
			Map<String, SimilarTermAA> similarTerms = resultItem.getSimilarTerms();
			
			double sum = 0.0;
			for (Map.Entry<String, SimilarTermAA> entry : similarTerms.entrySet()) {
				SimilarTermAA similarTerm = entry.getValue();
				Integer count = globalTermFreqs.get(entry.getKey());
				double idf = Math.log(itemSize/count);
				double tfidf = idf * similarTerm.getTf();
				similarTerm.setIdf(idf);
				similarTerm.setTfidf(tfidf);
				
				double value = tfidf * tfidf;
				sum += value;
			}
			double sqrtValue = Math.sqrt(sum);
			resultItem.setSqrtResult(sqrtValue);
		}
	}
	
	
}
