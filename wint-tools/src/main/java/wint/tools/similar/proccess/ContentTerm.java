package wint.tools.similar.proccess;

import java.io.Serializable;
import java.util.Map;

public class ContentTerm implements Serializable {
	
	private static final long serialVersionUID = 1645797540831228653L;

	private String id;
	
	private int termTotal;
	
	private Map<String, SimilarTerm> similarTerms;
	
	private double sqrtResult;
	
	public ContentTerm() {
		super();
		// 为了支持序列化而存在这个构造函数
	}
	
	public ContentTerm(String id) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public int getTermTotal() {
		return termTotal;
	}

	public void setTermTotal(int termTotal) {
		this.termTotal = termTotal;
	}

	public Map<String, SimilarTerm> getSimilarTerms() {
		return similarTerms;
	}

	public void setSimilarTerms(Map<String, SimilarTerm> similarTerms) {
		this.similarTerms = similarTerms;
	}

	public double getSqrtResult() {
		return sqrtResult;
	}

	public void setSqrtResult(double sqrtResult) {
		this.sqrtResult = sqrtResult;
	}

	public void setId(String id) {
		this.id = id;
	}

}
