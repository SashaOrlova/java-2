import java.util.function.Function;
import java.util.function.Supplier;

public class LightFutureImplementation<R> implements LightFuture<R> {
    private volatile boolean ready = false;
    private final Supplier<R> sup;
    private LightFutureImplementation parent;
    private R result;
    public final Object listener = new Object();

    LightFutureImplementation(Supplier<R> funct) {
        this(funct, null);
    }
    private <T> LightFutureImplementation(Supplier<R> funct, LightFutureImplementation<T> parent) {
        sup = funct;
        this.parent = parent;
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
}

class LightExecutionException extends RuntimeException {}