package org.cloud.utils;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import cn.hutool.core.bean.BeanUtil;

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
            return ((Collection<?>) obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
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

    public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();

        map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    public <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();

        map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /*
     * 取LinkedHashMap的子集
     * zakza
     * @param sortMap 源map
     * @param start   开始位置
     * @param end     结束位置
     * @return
     */
    @SneakyThrows
    public <K, V> LinkedHashMap<K, V> subMap(LinkedHashMap<K, V> sortMap, int start, int end) {
        if (end < start) {
            throw new Exception("end must less than start index");
        }
        List<Map.Entry<K, V>> lists = new ArrayList(sortMap.entrySet());
        LinkedHashMap<K, V> map = new LinkedHashMap();
        end = (end < lists.size() ? end : lists.size());
        for (Map.Entry<K, V> set : lists.subList(start, end)) {
            map.put(set.getKey(), set.getValue());
        }
        return map;
    }

    public <T> List<T> convertListToBean(Class<T> cls, List<? extends Map> mapList) {
        return mapList.stream().map(value -> BeanUtil.mapToBean(value,cls,true)).collect(Collectors.toList());
    }

}
