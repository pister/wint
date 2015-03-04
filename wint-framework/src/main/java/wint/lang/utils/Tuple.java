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

    public static <T1, T2> Tuple<T1, T2> create(T1 t1, T2 t2) {
        return new Tuple<T1, T2>(t1, t2);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;

        Tuple tuple = (Tuple) o;

        if (t1 != null ? !t1.equals(tuple.t1) : tuple.t1 != null) return false;
        if (t2 != null ? !t2.equals(tuple.t2) : tuple.t2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = t1 != null ? t1.hashCode() : 0;
        result = 31 * result + (t2 != null ? t2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "t1=" + t1 +
                ", t2=" + t2 +
                '}';
    }
}
