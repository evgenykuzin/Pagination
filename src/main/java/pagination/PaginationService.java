package pagination;

import com.google.gson.Gson;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import pageIterator.PageIterator;
import pageIterator.PageIteratorImpl;

import java.util.*;

public interface PaginationService {
    RedissonClient redisson = Redisson.create();
    Gson gson = new Gson();
    <E> PageIterator<E> makePagination(Collection<E> collection, Comparator<E> comparator, int maxPagination);

    static PaginationService getPaginationServiceImpl() {
        return new PaginationService() {
            @Override
            public <E> PageIterator<E> makePagination(Collection<E> collection, Comparator<E> comparator, int maxPagination) {
                SortedSet<E> sortedSet = new TreeSet<>(comparator);
                sortedSet.addAll(collection);
                var first = sortedSet.first();
                Class<E> classForSerialize = (Class<E>) first.getClass();
                var pageType = "default";
                String key = "page-type#" + pageType + "-id#" + Math.abs(new Random().nextLong());
                RList<String> rList = redisson.getList(key);
                sortedSet.forEach(p -> rList.add(gson.toJson(p)));
                return new PageIteratorImpl<>(key, comparator, classForSerialize, collection.size(), maxPagination);
            }
        };
    }

}
