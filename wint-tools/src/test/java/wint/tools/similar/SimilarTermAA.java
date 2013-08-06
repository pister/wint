package wint.tools.similar;

public class SimilarTermAA {
	
	private String term;
	
	private int count = 1;
	
	private double tf;
	
	private double idf;
	
	private double tfidf;

	public SimilarTermAA(String term) {
		super();
		this.term = term;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public void incrCount() {
		++count;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getTf() {
		return tf;
	}

	public void setTf(double tf) {
		this.tf = tf;
	}

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}

	public double getTfidf() {
		return tfidf;
	}

	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}

	@Override
	public String toString() {
		return "SimilarTerm [term=" + term + ", count=" + count + ", tf=" + tf + ", idf=" + idf + ", tfidf=" + tfidf + "]";
	}


}
