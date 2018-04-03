package ru.java.homework.threads;

import java.util.function.Function;
import java.util.function.Supplier;

/** Realization of LightFuture
 * @param <R> type of return value
 */
public class LightFutureImplementation<R> implements LightFuture<R> {
    private volatile boolean ready = false;
    private final Supplier<R> sup;
    private R result;

    /** create new LightFuture
     * @param fun supplier for computation
     */
    public LightFutureImplementation(Supplier<R> fun) {
        sup = fun;
    }

    /** return true if computation end, false otherwise
     * @return is computation end
     */
    @Override
    public boolean isReady() {
        return ready;
    }

    /** method returned result of computation, if computation not not end wait fir result
     * @return result of computation
     */
    @Override
    public R get() {
        if (!ready) {
            synchronized (sup) {
                while (!ready){
                    try {
                        sup.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
        return result;
    }

    /** start compute supplier
     */
    @Override
    public void evaluate() throws LightExecutionException {
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

    /** apply pass function to result of computation
     * @param func function for apply
     * @param <T> type of new result
     * @return new LightFuture
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> LightFuture<T> thenApply(Function<R, T> func) {
        return new LightFutureImplementation<>(() -> func.apply(this.get()));
    }

    public static class LightExecutionException extends Exception {}
}
