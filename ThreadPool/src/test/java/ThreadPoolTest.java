import static org.junit.Assert.*;

public class ThreadPoolTest {

    @org.junit.Test
    public void simpleAdd() throws Exception {
    ThreadPool threadPool = new ThreadPool(1);
    threadPool.add(new LightFutureImplementation<>(() -> 4));
    }

    @org.junit.Test
    public void twoThreadsAdd() throws Exception {
        ThreadPool threadPool = new ThreadPool(2);
        LightFuture<Integer> l1 = new LightFutureImplementation<>(() -> 1);
        LightFuture<Integer> l2 = new LightFutureImplementation<>(() -> 2);
        LightFuture<String> l3 = new LightFutureImplementation<>(() -> "Meow!");
        threadPool.add(l1);
        threadPool.add(l2);
        threadPool.add(l3);
        assertEquals((Integer) 1, l1.get());
        assertEquals((Integer) 2, l2.get());
        assertEquals("Meow!", l3.get());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void manyThreadsAdd() throws Exception {
        ThreadPool threadPool = new ThreadPool(4);
        LightFuture[] l = new LightFuture[1000];
        for (int i = 0 ; i < 1000; i++) {
            final int j = i;
            l[i] = new LightFutureImplementation(() -> j + 5);
            threadPool.add(l[i]);
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(i + 5, l[i].get());
        }
    }

    @org.junit.Test
    public void applyAll() throws Exception {
        ThreadPool threadPool = new ThreadPool(4);
        LightFuture<Integer> l1 = new LightFutureImplementation<>(() -> 5);
        LightFuture<Integer> l2 = l1.thenApply(x -> x*x);
        LightFuture<Integer> l3 = l2.thenApply(x -> x + 5);
        threadPool.add(l1);
        threadPool.add(l2);
        threadPool.add(l3);
        assertEquals((Integer)5, l1.get());
        assertEquals((Integer)25, l2.get());
        assertEquals((Integer)30, l3.get());
    }

    @org.junit.Test
    public void shutDownTest() throws Exception {
        ThreadPool threadPool = new ThreadPool(4);
        LightFuture<Integer> l1 = new LightFutureImplementation<>(() -> {
            while (true) {
            }
        });
        threadPool.add(l1);
        threadPool.shutdown();
        assertFalse(l1.isReady());
    }
}