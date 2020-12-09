import java.io.File;
import java.util.Objects;

public class User {
    String name;
    File avatar;

    public User(String name, File avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getAvatar() {
        return avatar;
    }

    public void setAvatar(File avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(avatar, user.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, avatar);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
