package utils;

import java.io.File;
import java.util.List;

//этот класс нужен лишь для тестирования и не несет смысловой нагрузки
public record PageImpl (int index,
        String title,
        List<User> users,
        List<String> strings) {
    //этот класс находится внутри этого класса только для уменьшения кода в проекте
    //сам по себе он нужен лишь для тестирования и не несет смысловой нагрузки
    public static record User (String name, String id, File file) { }
}
