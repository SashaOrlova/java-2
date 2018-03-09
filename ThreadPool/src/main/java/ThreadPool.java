import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class ThreadPool {
    private final Queue<LightFuture> queue = new ArrayDeque<>();
    private ArrayList<Thread> threads = new ArrayList<>();

    ThreadPool(int n) {
        for (int i = 0; i < n; i++) {
            Thread t = new Thread(new TaskWorker());
            threads.add(t);
            t.setDaemon(true);
            t.start();
        }
    }

    public <R> void add(LightFuture<R> r) {
        synchronized (queue) {
            queue.add(r);
        }
    }

    public void shutdown() {
        for (int i = 0 ; i < threads.size(); i++)
            threads.get(i).interrupt();
    }

    private final class TaskWorker implements Runnable {
        @Override
        public void run() {
            LightFuture task;
            while (!Thread.interrupted()) {
                synchronized (queue) {
                    task = queue.poll();
                }
                if (task != null)
                    task.evaluate();
            }
        }
    }

}
