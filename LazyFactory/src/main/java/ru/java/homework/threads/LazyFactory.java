package ru.java.homework.threads;

import java.util.function.Supplier;

public class LazyFactory {
    public static <T> Lazy<T> createThreadLazy(Supplier<T> sup) {
        return new MultiThreadLazy<>(sup);
    }

    public static <T> Lazy<T> createLazy(Supplier<T> sup) {
        return new OneThreadLazy<>(sup);
    }
}
