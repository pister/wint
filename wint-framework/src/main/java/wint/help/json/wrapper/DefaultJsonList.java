package wint.help.json.wrapper;

import wint.lang.convert.ConvertUtil;

import java.util.*;

/**
 * Created by songlihuang on 2020/6/4.
 */
public class DefaultJsonList implements JsonList {

    private List<Object> objectList;

    public DefaultJsonList(List<Object> objectList) {
        this.objectList = objectList;
    }

    @Override
    public int getIntValue(int index) {
        Integer value = getInteger(index);
        if (value == null) {
            return 0;
        }
        return value;
    }

    @Override
    public Integer getInteger(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Integer.class, null);
    }

    @Override
    public long getLongValue(int index) {
        Long value = getLong(index);
        if (value == null) {
            return 0;
        }
        return value;
    }

    @Override
    public Long getLong(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Long.class, null);
    }

    @Override
    public String getString(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    @Override
    public boolean getBooleanValue(int index) {
        Boolean value = getBoolean(index);
        if (value == null) {
            return false;
        }
        return value;
    }

    @Override
    public Boolean getBoolean(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Boolean.class, null);
    }

    @Override
    public Date getDate(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        return ConvertUtil.convertTo(o, Date.class, null);
    }

    @Override
    public JsonObject getObject(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        if (o instanceof Map) {
            return new DefaultJsonObject((Map)o);
        }
        return null;
    }


    @Override
    public JsonList getList(int index) {
        Object o = objectList.get(index);
        if (o == null) {
            return null;
        }
        if (o instanceof List) {
            return new DefaultJsonList((List)o);
        }
        return null;
    }

    @Override
    public int size() {
        return objectList.size();
    }

    @Override
    public boolean isEmpty() {
        return objectList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return objectList.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return new UnmodifiableIterator<Object>(objectList.iterator());
    }

    @Override
    public Object[] toArray() {
        return objectList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return objectList.toArray(a);
    }

    @Override
    public boolean add(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return objectList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object get(int index) {
        return objectList.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        return objectList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return objectList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return new UnmodifiableListIterator<Object>(objectList.listIterator());
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return new UnmodifiableListIterator<Object>(objectList.listIterator(index));
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return new DefaultJsonList(objectList.subList(fromIndex, toIndex));
    }

    @Override
    public String toString() {
        return "DefaultJsonList{" +
                "objectList=" + objectList +
                '}';
    }

    private static class UnmodifiableIterator<T> implements Iterator<T> {
        private Iterator<T> iterator;

        public UnmodifiableIterator(Iterator<T> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            return iterator.next();
        }

        public final void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static class UnmodifiableListIterator<T> implements ListIterator<T> {

        private ListIterator<T> target;

        public UnmodifiableListIterator(ListIterator<T> target) {
            this.target = target;
        }

        @Override
        public boolean hasNext() {
            return target.hasNext();
        }

        @Override
        public T next() {
            return target.next();
        }

        @Override
        public boolean hasPrevious() {
            return target.hasPrevious();
        }

        @Override
        public T previous() {
            return target.previous();
        }

        @Override
        public int nextIndex() {
            return target.nextIndex();
        }

        @Override
        public int previousIndex() {
            return target.previousIndex();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }

}
