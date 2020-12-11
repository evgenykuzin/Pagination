import deprecated.PaginationManager;
import org.junit.Assert;
import org.junit.Test;
import pageIterator.PageIterator;
import pagination.PaginationService;
import utils.PageImpl;

import java.io.File;
import java.util.*;

public class Tests {


    @Test
    @Deprecated
    public void deprecatedTest() {
        try (PaginationManager<PageImpl> pagePaginationManager = new PaginationManager<>()) {
            Set<PageImpl> pageImpls = new HashSet<>();
            for (int i = 0; i < 20; i++) {
                pageImpls.add(constructTestPage(i));
            }
            String key;
            key = pagePaginationManager.makePaginationR(pageImpls, Comparator.comparingInt(PageImpl::index));
            Iterator<Collection<PageImpl>> pageIterator = pagePaginationManager.getIterator(key, 10, 20);
            Set<PageImpl> testCollection = new HashSet<>();
            while (pageIterator.hasNext()) {
                var nextPage = pageIterator.next();
                testCollection.addAll(nextPage);
            }
            Assert.assertEquals(pageImpls, testCollection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWithNumbers() {
        PaginationService paginationService = PaginationService.getPaginationServiceImpl();
        Comparator<Integer> comparator = Comparator.naturalOrder();
        Set<Integer> pageImpls = new HashSet<>();
        int maxPages = 103;
        int maxPagination = 10;
        String userId = "someUserId";
        //init data
        for (int i = 1; i <= maxPages; i++) {
            pageImpls.add(i);
        }
        Set<Integer> testSet = new HashSet<>();
        //fill testSet with "next" sublists
        try (PageIterator<Integer> pageIterator =
                     paginationService.makePagination(pageImpls, comparator, maxPagination)) {
            while (pageIterator.hasNext(userId)) {
                var nextPage = pageIterator.next(userId);
                testSet.addAll(nextPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //assert
        Assert.assertEquals(pageImpls, testSet);
    }

    @Test
    public void testWithPageImpls() {
        PaginationService paginationService = PaginationService.getPaginationServiceImpl();
        Comparator<PageImpl> comparator = Comparator.comparingInt(PageImpl::index);
        Set<PageImpl> pageImpls = new HashSet<>();
        int maxPages = 71;
        int maxPagination = 50;
        String userId = "someUserId";
        //init data
        for (int i = 1; i <= maxPages; i++) {
            pageImpls.add(constructTestPage(i));
        }
        Set<PageImpl> testSet = new HashSet<>();
        //fill testSet with "next" sublists
        try (PageIterator<PageImpl> pageIterator =
                     paginationService.makePagination(pageImpls, comparator, maxPagination)) {
            while (pageIterator.hasNext(userId)) {
                var nextPage = pageIterator.next(userId);
                testSet.addAll(nextPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //assert
        Assert.assertEquals(pageImpls, testSet);
    }

    PageImpl constructTestPage(int n) {
        String title = "title_" + n;
        List<PageImpl.User> users = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new PageImpl.User("name_" + n + "_" + i, String.valueOf(i), new File("")));
        }
        for (int i = 0; i < 100; i++) {
            strings.add("string_" + n + "_" + i);
        }
        return new PageImpl(n, title, users, strings);
    }
}
