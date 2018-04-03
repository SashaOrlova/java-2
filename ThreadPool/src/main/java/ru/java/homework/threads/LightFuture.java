package ru.java.homework.threads;

import java.util.function.Function;

/** Abstract interface for LightFuture
 * @param <R> type of return value
 */
public interface LightFuture<R> {
    /** return true if computation end, false otherwise
     * @return is computation end
     */
    boolean isReady();

    /** method returned result of computation, if computation not not end wait fir result
     * @return result of computation
     */
    R get();

    /** start compute supplier
     */
    void evaluate() throws LightFutureImplementation.LightExecutionException;

    /** apply pass function to result of computation
     * @param func function for apply
     * @param <T> type of new result
     * @return new LightFuture
     */
    <T> LightFuture<T> thenApply(Function<R, T> func);
}
