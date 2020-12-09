import java.util.List;
import java.util.Objects;

@Page(pageType = "friends")
public class PageImpl {
    int index;
    String title;
    List<User> users;
    List<String> strings;

    public PageImpl(int index, String title, List<User> users, List<String> strings) {
        this.index = index;
        this.title = title;
        this.users = users;
        this.strings = strings;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageImpl pageImpl = (PageImpl) o;
        return index == pageImpl.index && Objects.equals(title, pageImpl.title) && Objects.equals(users, pageImpl.users) && Objects.equals(strings, pageImpl.strings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, title, users, strings);
    }

    @Override
    public String toString() {
        return "Page{" +
                "index=" + index +
                ", title='" + title + '\'' +
                ", users=" + users +
                ", strings=" + strings +
                '}';
    }
}
