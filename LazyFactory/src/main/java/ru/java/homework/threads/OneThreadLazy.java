package ru.java.homework.threads;

import java.util.function.Supplier;

/** Realisation of Lazy used by one thread
 * @param <T>
 */
class OneThreadLazy<T> implements Lazy<T> {
    private Supplier<T> sup;
    private T ans;

    /** Create new Lazy
     * @param sup supplier returned result
     */
    public OneThreadLazy(Supplier<T> sup) {
        this.sup = sup;
    }

    @Override
    public T get() {
        if (sup != null) {
            ans = sup.get();
            sup = null;
        }
        return ans;
    }
}
