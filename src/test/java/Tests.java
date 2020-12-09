import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.*;

public class Tests {

    @Test
    public void test() {
        PaginationManager<PageImpl> pagePaginationManager = new PaginationManager<>();
        Set<PageImpl> pageImpls = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            pageImpls.add(constructTestPage(i));
        }
        String key;
        key = pagePaginationManager.makePaginationR(pageImpls, Comparator.comparingInt(p -> p.index));
        Iterator<Collection<PageImpl>> pageIterator = pagePaginationManager.getIterator(key, 10, 20);
        Set<PageImpl> testCollection = new HashSet<>();
        while (pageIterator.hasNext()) {
            var nextPage = pageIterator.next();
            testCollection.addAll(nextPage);
        }
        Assert.assertEquals(pageImpls, testCollection);
    }

    @Test
    public void test2() {
        PaginationManager<Integer> pagePaginationManager = new PaginationManager<>();
        SortedSet<Integer> pageImpls = new TreeSet<>(Integer::compareTo);
        for (int i = 0; i < 20; i++) {
            pageImpls.add(i);
        }
        System.out.println(pageImpls);
        String key;
        key = pagePaginationManager.makePaginationR(pageImpls);
        Iterator<Collection<Integer>> pageIterator = pagePaginationManager.getIterator(key, 10, 20);
        Set<Integer> testCollection = new HashSet<>();
        while (pageIterator.hasNext()) {
            var nextPage = pageIterator.next();
            System.out.println(nextPage);
            testCollection.addAll(nextPage);
        }
        System.out.println("result");
        System.out.println(testCollection);
        Assert.assertEquals(pageImpls, testCollection);
    }

    PageImpl constructTestPage(int n) {
        String title = "title_" + n;
        List<User> users = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new User("name_" + n + "_" + i, new File("")));
        }
        for (int i = 0; i < 100; i++) {
            strings.add("string_" + n + "_" + i);
        }
        return new PageImpl(n, title, users, strings);
    }
}
