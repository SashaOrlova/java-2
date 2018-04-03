package ru.java.homework.threads;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Class for make multithreaded computations
 */
public class ThreadPool {
    private final Queue<LightFuture> queue = new ArrayDeque<>();
    private ArrayList<Thread> threads = new ArrayList<>();

    /** create new ThreadPool with adjusted number of threads
     * @param n - number of threads
     */
    public ThreadPool(int n) {
        for (int i = 0; i < n; i++) {
            Thread t = new Thread(new TaskWorker());
            threads.add(t);
            t.setDaemon(true);
            t.start();
        }
    }

    /** add new task at threadpool
     * @param r - new task
     * @param <R> - return type of task
     */
    public <R> void add(LightFuture<R> r) {
        synchronized (queue) {
            queue.add(r);
        }
    }

    /**
     * turn off threads using Thread.interrupt
     */
    public void shutdown() {
        threads.forEach(Thread::interrupt);
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
                    try {
                        task.evaluate();
                    } catch (LightFutureImplementation.LightExecutionException ignored) {
                    }
            }
        }
    }

}
