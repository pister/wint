package wint.lang.collections;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import wint.lang.utils.CollectionUtil;

public class FastStack<T> implements Iterable<T> {

	private List<T> datas = CollectionUtil.newArrayList();
	
	public FastStack(List<T> initDatas) {
		super();
		if (CollectionUtil.isEmpty(initDatas)) {
			return;
		}
		List<T> reverse = CollectionUtil.newArrayList(initDatas);
		Collections.reverse(reverse);
		for (T t : reverse) {
			push(t);
		}
	}
	
	public Iterator<T> iterator() {
		return datas.iterator();
	}
	
	public FastStack() {
		super();
	}

	public void push(T t) {
		datas.add(t);
	}
	
	public T top() {
		return datas.get(size() - 1);
	}
	
	public T pop() {
		return datas.remove(size() - 1);
	}
	
	public boolean isEmpty() {
		return datas.isEmpty();
	}
	
	public int size() {
		return datas.size();
	}
	
	public String toString() {
		return datas.toString();
	}
	
}
