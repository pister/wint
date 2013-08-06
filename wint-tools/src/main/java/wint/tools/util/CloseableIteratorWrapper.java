package wint.tools.util;

import java.util.Iterator;

public class CloseableIteratorWrapper<E> implements CloseableIterator<E> {
	
	private Iterator<E> iterator;

	public CloseableIteratorWrapper(Iterator<E> iterator) {
		super();
		this.iterator = iterator;
	}

	public boolean hasNext() {
		return iterator.hasNext();
	}

	public E next() {
		return iterator.next();
	}

	public void remove() {
		iterator.remove();
	}
	
	public void close() {
		
	}

}
