package org.cloud.utils;


import java.util.LinkedList;
import java.util.List;
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

}
