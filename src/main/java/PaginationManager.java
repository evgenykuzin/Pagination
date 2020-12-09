import com.google.gson.Gson;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.util.*;

public class PaginationManager<E> implements AutoCloseable{
    private final RedissonClient redisson;
    private final Gson gson = new Gson();
    private Class<?> classForSerialize;
    private Comparator<? super E> comparator;

    public PaginationManager() {
        redisson = Redisson.create();
    }

    public String makePaginationR(SortedSet<E> sortedSet) {
        this.comparator = sortedSet.comparator();
        var first = sortedSet.first();
        if (classForSerialize == null) classForSerialize = first.getClass();
        Page annotation = classForSerialize.getAnnotation(Page.class);
        var pageType = "default";
        if (annotation != null) {
            pageType = annotation.pageType();
        }
        String key = "page-type#" + pageType + "-id#" + Math.abs(new Random().nextLong());
        System.out.println(key);
        cache(sortedSet, key);
        return key;
    }

    public String makePaginationR(Collection<E> collection, Comparator<E> comparator) {
        this.comparator = comparator;
        SortedSet<E> sortedSet = new TreeSet<>(comparator);
        sortedSet.addAll(collection);
        return makePaginationR(sortedSet);
    }

    private void cache(Set<E> set, String key) {
        RList<String> rList = redisson.getList(key);
        set.forEach(p -> rList.add(gson.toJson(p)));
    }

    private Collection<E> getSublist(String key, int currentIndex, int maxPagination) throws EmptyPaginationIteratorExceprion {
        RList<String> jsons = redisson.getList(key);
        List<String> subJsons = jsons.range(currentIndex, currentIndex+maxPagination);
        if (subJsons == null || subJsons.isEmpty() || classForSerialize == null) throw new EmptyPaginationIteratorExceprion();
        Collection<E> collection = new TreeSet<>(comparator);
        subJsons.forEach(json -> {
            if (json != null) {
                E element = (E) gson.fromJson(json, classForSerialize);
                collection.add(element);
            }
        });
        return collection;
    }

    private void removeObject(String key, int currentIndex) {
        redisson.getList(key).remove(currentIndex);
    }

    @Override
    public void close() throws Exception {
        redisson.shutdown();
    }

    public Iterator<Collection<E>> getIterator(String key, int maxPagination, int maxPages) {
        return new PaginationIterator(key, maxPagination, maxPages);
    }

    class PaginationIterator implements Iterator<Collection<E>>{
        String key;
        private int currentIndex;
        private final int maxPagination;
        private final int maxPages;

        public PaginationIterator(String key, int maxPagination, int maxPages) {
            this.key = key;
            this.maxPagination = maxPagination;
            this.maxPages = maxPages;
            this.currentIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < maxPages && currentIndex >= 0;
        }

        @Override
        public Collection<E> next() {
            Collection<E> elements;
            try {
                elements = getSublist(key, currentIndex, maxPagination);
                currentIndex+=maxPagination+1;
            } catch (EmptyPaginationIteratorExceprion ex) {
                ex.printStackTrace();
                currentIndex = -1;
                return null;
            }

            return elements;
        }

        @Override
        public void remove() {
            removeObject(key, currentIndex);
            if (currentIndex > 0) currentIndex--;
        }
    }

}
