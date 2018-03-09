package ru.java.homework.threads;

import java.util.function.Function;

public interface LightFuture<R> {
    boolean isReady();
    R get();
    void evaluate();
    <T> LightFuture<T> thenApply(Function<R, T> func);
}
