package wint.tools.similar;

import java.io.File;
import java.util.Map;

public class ResultItem {
	
	private File file;
	
	private int termTotal;
	
	private Map<String, SimilarTermAA> similarTerms;
	
	private double sqrtResult;
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public int getTermTotal() {
		return termTotal;
	}

	public void setTermTotal(int termTotal) {
		this.termTotal = termTotal;
	}

	public Map<String, SimilarTermAA> getSimilarTerms() {
		return similarTerms;
	}

	public void setSimilarTerms(Map<String, SimilarTermAA> similarTerms) {
		this.similarTerms = similarTerms;
	}

	public double getSqrtResult() {
		return sqrtResult;
	}

	public void setSqrtResult(double sqrtResult) {
		this.sqrtResult = sqrtResult;
	}

}
