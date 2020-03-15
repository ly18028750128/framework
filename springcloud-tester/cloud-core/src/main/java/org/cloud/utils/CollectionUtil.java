package org.cloud.utils;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CollectionUtil {
    private CollectionUtil() {
    }

    private static class Handler {
        private Handler() {
        }

        private static CollectionUtil handler = new CollectionUtil();
    }

    public static CollectionUtil single() {
        return Handler.handler;
    }

    public final static Integer _MAX_LIST_LENGTH = 100;

    /**
     * 分割List
     *
     * @param originalList
     * @param maxLength
     * @param <E>
     * @return
     */
    public <E> List<List<E>> spitList(List<E> originalList, final Integer maxLength) {
        int limit = NumericUtil.single().countStep(originalList.size(), maxLength);

        List<List<E>> resultList = new LinkedList<List<E>>();
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
            resultList.add(originalList.stream().skip(i * maxLength).limit(maxLength).collect(Collectors.toList()));
        });

        return resultList;
    }

    public <E> List<List<E>> spitList(List<E> originalList) {
        return this.spitList(originalList, _MAX_LIST_LENGTH);
    }

    public boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof CharSequence) {
            return "".equals(obj.toString());
        } else if (obj instanceof Object[]) {
            return ((Object[]) obj).length < 1;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        return false;
    }

    public boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public Object[] toArray(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).toArray(new Object[]{});
        } else if (obj instanceof Object[]) {
            return (Object[]) obj;
        }
        return new Object[]{obj};
    }

}
