package wint.lang.magic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

import wint.lang.exceptions.MagicException;
import wint.lang.utils.CollectionUtil;

/**
 * @author pister 2011-12-22 10:12:16
 */
public class MagicList<T> extends ArrayList<T> {
	
	private static final long serialVersionUID = -4664806599967661564L;

	protected MagicList() {}
	
	protected MagicList(int cap) {
		super(cap);
	}
	
	public String join(String token) {
		return join(token, null);
	}
	
	public String join(String token, Transformer<T, String> valueTransformer) {
		return CollectionUtil.join(this, token, valueTransformer);
	}
	
	public static <T> MagicList<T> newList(T ...objects) {
		if (objects == null || objects.length == 0) {
			return newList();
		}
		MagicList<T> ret = newList();
		for (T object : objects) {
			ret.add(object);
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> MagicList<T> wrap(Object object) {
		if (object == null) {
			return newList();
		}
		if (object.getClass().isArray()) {
			return copyFromArray(object);
		}
		if (object instanceof Iterable<?>) {
			return copyFromIterable(object);
		}
		MagicList<T> ret = newList();
		ret.add((T)object);
		return ret;
	}
	
	public static <T> MagicList<MagicList<T>> wrapArray(Object object) {
		if (object == null) {
			return newList();
		}
		MagicList<MagicList<T>> ret = newList();
		if (object.getClass().isArray()) {
			MagicList<T> array = copyFromArray(object);
			ret.add(array);
			return ret;
		}
		if (object instanceof Collection<?>) {
			MagicList<T> array = copyFromIterable(object);
			ret.add(array);
			return ret;
		}
		throw new MagicException(object + " is not an array or collection!");
	}
	
	public static <T> MagicList<T> newList() {
		return new MagicList<T>();
	}
	
	public static <T> MagicList<T> newList(int cap) {
		return new MagicList<T>(cap);
	}
	
	
	@SuppressWarnings("unchecked")
	private static <T> MagicList<T> copyFromArray(Object array) {
		MagicList<T> magicList = MagicList.newList();
		for (int i = 0, len = Array.getLength(array); i < len; ++i) {
			T item = (T)Array.get(array, i);
			magicList.add(item);
		}
		return magicList;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> MagicList<T> copyFromIterable(Object object) {
		MagicList<T> magicList = MagicList.newList();
		Iterable<?> c = (Iterable<?>)object;
		for (Object item : c) {
			magicList.add((T)item);
		}
		return magicList;
	}


}
