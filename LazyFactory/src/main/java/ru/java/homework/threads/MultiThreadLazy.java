package ru.java.homework.threads;

import java.util.function.Supplier;

public class MultiThreadLazy<T> implements Lazy<T> {
    volatile private Supplier<T> sup;
    volatile private T ans;

    public MultiThreadLazy(Supplier<T> sup) {
        this.sup = sup;
    }

    @Override
    public T get() {
        if (sup == null) {
            return ans;
        }
        synchronized (this) {
            if (sup != null) {
                ans = sup.get();
                sup = null;
            }
        }
        return ans;
    }
}
