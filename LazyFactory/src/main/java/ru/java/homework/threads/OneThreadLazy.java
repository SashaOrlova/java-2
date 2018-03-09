package ru.java.homework.threads;

import java.util.function.Supplier;

public class OneThreadLazy<T> implements Lazy<T> {
    private Supplier<T> sup;
    private T ans;

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
