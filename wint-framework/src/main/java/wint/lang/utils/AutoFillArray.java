package wint.lang.utils;

import java.util.Iterator;

import wint.lang.magic.MagicList;

public class AutoFillArray<T> implements Iterable<T> {

	private MagicList<T> elements = MagicList.newList(4);
	
	private T defaultFillObject = null;
	
	private boolean ignoreZeroTail;
	
	public AutoFillArray(boolean ignoreZeroTail) {
		super();
		this.ignoreZeroTail = ignoreZeroTail;
	}

	public AutoFillArray() {
		this(true);
	}

	public Object getDefaultFillObject() {
		return defaultFillObject;
	}

	public void setDefaultFillObject(T defaultFillObject) {
		this.defaultFillObject = defaultFillObject;
	}
	
	protected boolean canIgnore(T value) {
		if (value == null) {
			return true;
		}
		if (String.valueOf(value).equals("0")) {
			return true;
		}
		return false;
	}

	public void set(int pos, T value) {
		if (ignoreZeroTail && pos >= elements.size()) {
			if (canIgnore(value)) {
				return;
			}
		}
		while (elements.size() - 1 < pos) {
			elements.add(defaultFillObject);
		}
		elements.set(pos, value);
	}
	
	public int size() {
		return elements.size();
	}
	
	public T get(int pos) {
		return elements.get(pos);
	}
	
	public String join(String forDefaultElement, String token) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (T t : elements) {
			if (first) {
				first = false;
			} else {
				sb.append(token);
			}
			if (t == defaultFillObject) {
				sb.append(forDefaultElement);
			} else {
				sb.append(t);
			}
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return elements.toString();
	}

	public Iterator<T> iterator() {
		return elements.iterator();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
}
