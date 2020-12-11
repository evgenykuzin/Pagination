package pageIterator;

import com.google.gson.Gson;
import exceptions.EmptyPageIteratorException;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.util.*;

public interface PageIterator<E> extends AutoCloseable{
    RedissonClient redisson = Redisson.create();
    Collection<E> next(String userId);
    boolean hasNext(String userId);
    Collection<E> last();
    String key();

    default Collection<E> subset(String key, Comparator<E> comparator,
                                 Class<E> classForSerialize,
                                 int start, int end) throws EmptyPageIteratorException {
        Gson gson = new Gson();
        RList<String> jsons = redisson.getList(key);
        List<String> subJsons = jsons.range(start, end);
        if (subJsons == null || subJsons.isEmpty()) throw new EmptyPageIteratorException();
        Collection<E> collection = new TreeSet<>(comparator);

        subJsons.forEach(json -> {
            if (json != null) {
                E element = gson.fromJson(json, classForSerialize);
                collection.add(element);
            }
        });
        return collection;
    }

    @Override
    default void close() throws Exception {
        //и что-то еще здесь может закрываться
        redisson.shutdown();
    }
}
