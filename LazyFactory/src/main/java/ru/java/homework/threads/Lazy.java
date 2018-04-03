package ru.java.homework.threads;

/** Abstract interface for Lazy computation
 * @param <T> type of return value
 */
public interface Lazy<T> {
    /** if computations end return result, else make computations and return result
     * @return result of computation
     */
    T get();
}
