package pw.swordfish.diag;

import java.util.function.Predicate;

public class Contract {
    public static <T> T ensuresNotNull(T t, String name) {
        if (t == null)
            throw new IllegalArgumentException("name");
        return t;
    }

    public static <T> T ensures(Predicate<T> p, T t, String name) {
        if (! p.test(t))
            throw new IllegalArgumentException(name);
        return t;
    }
}