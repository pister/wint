package wint.tools.util;

import java.util.Iterator;

public interface CloseableIterator<E> extends Iterator<E> {
	
	void close();

}
