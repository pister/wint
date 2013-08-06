package wint.lang.utils;

import java.io.Serializable;

public class Tuple<T1, T2> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4845753270083028548L;

	private T1 t1;
	
	private T2 t2;

	public Tuple(T1 t1, T2 t2) {
		super();
		this.t1 = t1;
		this.t2 = t2;
	}

	public Tuple() {
		super();
	}

	public T1 getT1() {
		return t1;
	}

	public void setT1(T1 t1) {
		this.t1 = t1;
	}

	public T2 getT2() {
		return t2;
	}

	public void setT2(T2 t2) {
		this.t2 = t2;
	}
	
}
