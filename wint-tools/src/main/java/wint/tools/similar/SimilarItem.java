package wint.tools.similar;

import java.io.Serializable;

public class SimilarItem implements Serializable {

	private static final long serialVersionUID = -4387158539494884672L;

	private String id;
	
	private double similarValue;

	public SimilarItem() {
		super();
	}

	public SimilarItem(String id, double similarValue) {
		super();
		this.id = id;
		this.similarValue = similarValue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getSimilarValue() {
		return similarValue;
	}

	public void setSimilarValue(double similarValue) {
		this.similarValue = similarValue;
	}

	@Override
	public String toString() {
		return "SimilarItem [id=" + id + ", similarValue=" + similarValue + "]";
	}
	
}
