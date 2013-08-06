package wint.tools.cache;

import java.io.Serializable;

/**
 * @author longyi.hsl
 * 2012-6-28 上午10:32:06
 */
public class Entry<V> implements Serializable {

	private static final long serialVersionUID = -2791846210989689733L;
	
	private V object;

	public Entry() {
		super();
	}

	public Entry(V object) {
		super();
		this.object = object;
	}

	public V getObject() {
		return object;
	}

	public void setObject(V object) {
		this.object = object;
	}

    @Override
    public String toString() {
        return "Entry{" +
                "object=" + object +
                '}';
    }
}
