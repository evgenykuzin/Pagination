package pageIterator;

import exceptions.EmptyPageIteratorException;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PageIteratorImpl<E> implements PageIterator<E> {
    private final String key;
    private final Comparator<E> comparator;
    private final Class<E> classForSerialize;
    private final int maxPages;
    private final int maxPagination;
    private final Map<Object, Integer> indexMap;

    public PageIteratorImpl(String key, Comparator<E> comparator, Class<E> classForSerialize, int maxPages, int maxPagination) {
        this.key = key;
        this.comparator = comparator;
        this.classForSerialize = classForSerialize;
        this.maxPages = maxPages;
        this.maxPagination = maxPagination;
        indexMap = new HashMap<>();
    }

    private int nextIndex(Object userId) {
        Integer result = indexMap.computeIfPresent(userId, (k, v) -> v+=maxPagination);
        if (result == null) {
            result = 0;
            indexMap.put(userId, 0);
        }
        return result;
    }

    private int currentIndex(Object userId) {
        var i = indexMap.get(userId);
        return i == null? 0 : i;
    }

    @Override
    public Collection<E> next(String userId) {
        try {
            int start = nextIndex(userId);
            return subset(key, comparator, classForSerialize,
                    start, maxPagination - 1 + start);
        } catch (EmptyPageIteratorException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean hasNext(String userId) {
        int index = currentIndex(userId);
        return index >= 0 && index+maxPagination <= maxPages;
    }

    @Override
    public Collection<E> last() {
        try {
            return subset(key, comparator, classForSerialize, maxPages - maxPagination, maxPages);
        } catch (EmptyPageIteratorException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String key() {
        return key;
    }
}
