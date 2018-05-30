package testClasses;

import ru.java.xunit.annotations.Test;

import java.io.IOException;

public class Test3 {
    @Test(expected = IOException.class)
    public void test1() throws Exception {
        throw new IOException();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void test2() throws Exception {
        throw new ArrayIndexOutOfBoundsException();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test3() throws Exception {
        throw new IllegalArgumentException();
    }

    @Test(ignore = "ignored")
    public void test4() {}

    @Test(ignore = "ignored")
    public void test5() {}

    public void notTest() {}

    @Test(ignore = "ignored")
    public void ignoredTest() {}
}
