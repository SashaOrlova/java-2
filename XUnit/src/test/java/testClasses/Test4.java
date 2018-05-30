package testClasses;

import ru.java.xunit.annotations.Test;

public class Test4 {

    @Test
    public void test4() {
        throw new IllegalArgumentException();
    }

    @Test
    public void test5() {
        throw new RuntimeException();
    }

    public void notTest() {}

    @Test
    public void rightTest() {}

    @Test(ignore = "not a test")
    public void ignoredTest() {}
}
