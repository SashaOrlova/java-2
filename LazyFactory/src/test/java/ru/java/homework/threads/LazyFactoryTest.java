package ru.java.homework.threads;

import java.util.Random;

import static org.junit.Assert.*;

public class LazyFactoryTest {
    class Counter {
        int c = 0;
    }

    @org.junit.Test
    public void createThreadLazy() throws Exception {
        Random rand = new Random();
        Lazy<Integer> l = LazyFactory.createLazy(rand::nextInt);
        int first = l.get();
        Thread t1 = new Thread(() -> { l.get();
            l.get();
            l.get();
        });
        Thread t2 = new Thread(() -> { l.get();
            l.get();
            l.get();
        });
        t1.start();
        t2.start();
        assertEquals((Integer) first, l.get());
    }

    @org.junit.Test
    public void simpleCreateLazy() throws Exception {
        Lazy<Integer> l = LazyFactory.createLazy(() -> 7);
        assertEquals((Integer) 7, l.get());
    }

    @org.junit.Test
    public void countCreateLazy() throws Exception {
        Counter c = new Counter();
        Lazy<Integer> l = LazyFactory.createLazy(() -> c.c++);
        l.get();
        l.get();
        l.get();
        assertEquals(1, c.c);
    }
}