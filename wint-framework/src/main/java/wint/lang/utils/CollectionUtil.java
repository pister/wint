package wint.lang.utils;

import java.util.*;

import wint.lang.magic.Transformer;
import wint.lang.utils.filter.Filter;
import wint.lang.utils.merger.Mapper;

public class CollectionUtil {
	
	public static boolean isEmpty(Collection<?> c) {
		if (c == null || c.isEmpty()) {
			return true;
		}
		return false;
	}

	public static <T> ArrayList<T> newArrayList() {
		return new ArrayList<T>();
	}
	
	public static <T> LinkedList<T> newLinkedList() {
		return new LinkedList<T>();
	}
	
	public static <T> LinkedList<T> newLinkedList(Collection<T> initData) {
		return new LinkedList<T>(initData);
	}
	
	public static <T> LinkedList<T> newLinkedList(T[] initData) {
		LinkedList<T> ret = new LinkedList<T>();
		for (T t : initData) {
			ret.add(t);
		}
		return ret;
	}
	
	public static <T> ArrayList<T> newArrayList(int initialCapacity) {
		return new ArrayList<T>(initialCapacity);
	}
	
	public static <T> ArrayList<T> newArrayList(Collection<T> initData) {
		return new ArrayList<T>(initData);
	}
	
	public static <T> ArrayList<T> newArrayList(T[] initData) {
		ArrayList<T> ret = new ArrayList<T>(initData.length);
		for (T t : initData) {
			ret.add(t);
		}
		return ret;
	}
	
	public static <T> HashSet<T> newHashSet() {
		return new HashSet<T>();
	}
	
	public static <T> HashSet<T> newHashSet(Collection<T> initData) {
		return new HashSet<T>(initData);
	}

    public static <T> List<T> dup(T o, int count) {
        List<T> ret = newArrayList(count);
        for (int i = 0; i < count; ++i) {
            ret.add(o);
        }
        return ret;
    }

    public static <T> String join(Collection<T> c, String token, Transformer<T, String> valueTransformer) {
        return join(c, token, valueTransformer, null);
    }
	
	public static <T> String join(Collection<T> c, String token, Transformer<T, String> valueTransformer, Filter<T> filter) {
        if (c == null) {
            return StringUtil.EMPTY;
        }
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (T object : c) {
            if (filter != null && !filter.accept(object)) {
                continue;
            }
			if (first) {
				first = false;
			} else {
				sb.append(token);
			}
			if (valueTransformer != null) {
				sb.append(valueTransformer.transform(object));
			} else {
				sb.append(object);
			}
		}
		return sb.toString();
	}
	
	public static <T> String join(Collection<T> c, String token) {
		return join(c, token, null, null);
	}

    public static <T> String join(Collection<T> c, String token, Filter<T> filter) {
        return join(c, token, null, filter);
    }

    public static <T> List<T> filter(Collection<T> input, Filter<T> filter) {
        if (CollectionUtil.isEmpty(input)) {
            return CollectionUtil.newArrayList(0);
        }
        List<T> ret = CollectionUtil.newArrayList(input.size());
        for (T o : input) {
            if (filter != null && !filter.accept(o)) {
                continue;
            }
            ret.add(o);
        }
        return ret;
    }

    public static <S, T> List<T> transformList(Collection<S> input, Transformer<S, T> transformer) {
        return transformList(input, transformer, null);
    }

    public static <S, T> List<T> transformListFilterNull(Collection<S> input, Transformer<S, T> transformer) {
        return transformList(input, transformer, new Filter<S>() {
            @Override
            public boolean accept(S s) {
                return s != null;
            }
        });
    }

    /**
     * 把一个list转成另外类型的list
     * @param input
     * @param transformer
     * @param filter
     * @param <S>
     * @param <T>
     * @return
     */
	public static <S, T> List<T> transformList(Collection<S> input, Transformer<S, T> transformer, Filter<S> filter) {
        if (CollectionUtil.isEmpty(input)) {
            return CollectionUtil.newArrayList(0);
        }
		List<T> ret = CollectionUtil.newArrayList(input.size());
		for (S o : input) {
            if (filter != null && !filter.accept(o)) {
                continue;
            }
			ret.add(transformer.transform(o));
		}
		return ret;
	}


    /**
     * 通过
     * @param input
     * @param mapper
     * @param <S>
     * @param <K>
     * @param <D>
     */
    public static <S, K, D> void merge(Collection<S> input, Mapper<S, K, D> mapper) {
        if (CollectionUtil.isEmpty(input)) {
            return;
        }
        List<K> keys = CollectionUtil.newArrayList(input.size());
        for (S s : input) {
            K key = mapper.toKey(s);
            keys.add(key);
        }
        Map<K, D> destPool = mapper.toDestPool(keys).toMap();
        for (S s : input) {
            K key = mapper.toKey(s);
            D dest = destPool.get(key);
            mapper.setDest(s, dest);
        }
    }

}
