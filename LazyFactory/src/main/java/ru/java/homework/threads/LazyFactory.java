package ru.java.homework.threads;

import java.util.function.Supplier;

/**
 * Class for create Lazy objects
 */
public class LazyFactory {
    /** create threadsafe Lazy
     * @param sup task for Lazy
     * @param <T> type of supplier
     * @return new Lazy contained supplier
     */
    public static <T> Lazy<T> createThreadLazy(Supplier<T> sup) {
        return new MultiThreadLazy<>(sup);
    }

    /** create Lazy for one thread
     * @param sup task for Lazy
     * @param <T> type of supplier
     * @return new Lazy contained supplier
     */
    public static <T> Lazy<T> createLazy(Supplier<T> sup) {
        return new OneThreadLazy<>(sup);
    }
}
