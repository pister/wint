package wint.tools.similar.proccess;

import java.io.Serializable;

public class SimilarTerm implements Serializable {
	
	private static final long serialVersionUID = 4146275390147769110L;

	private String term;
	
	private int count = 1;
	
	private double tf;
	
	private double idf;
	
	private double tfidf;

	public SimilarTerm() {
		super();
	}

	public SimilarTerm(String term) {
		super();
		this.term = term;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void incrCount() {
		this.count ++;
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

}
