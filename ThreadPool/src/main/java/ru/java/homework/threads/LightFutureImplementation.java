package ru.java.homework.threads;

import java.util.function.Function;
import java.util.function.Supplier;

public class LightFutureImplementation<R> implements LightFuture<R> {
    private volatile boolean ready = false;
    private final Supplier<R> sup;
    private R result;

    public LightFutureImplementation(Supplier<R> funct) {
        sup = funct;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public R get() {
        if (!ready) {
            synchronized (sup) {
                while (!ready){
                    try {
                        sup.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void evaluate() {
        synchronized (sup) {
        if (!ready) {
            try {
                result = sup.get();
            } catch (RuntimeException e) {
                throw new LightExecutionException();
            }
            ready = true;
            }
        sup.notify();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LightFuture<T> thenApply(Function<R, T> func) {
        return new LightFutureImplementation<T>(() -> func.apply(this.get()));
    }

    public static class LightExecutionException extends RuntimeException {}
}
