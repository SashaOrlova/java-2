package ru.java.homework.threads;

import java.util.function.Supplier;

/** Realisation of Lazy used by many threads
 * @param <T>
 */
class MultiThreadLazy<T> implements Lazy<T> {
    volatile private Supplier<T> sup;
    volatile private T ans;

    /** Create new Lazy
     * @param sup supplier returned result
     */
    public MultiThreadLazy(Supplier<T> sup) {
        this.sup = sup;
    }

    @Override
    public T get() {
        if (sup != null) {
            synchronized (this) {
                if (sup != null) {
                    ans = sup.get();
                    sup = null;
                }
            }
        }
        return ans;
    }
}
