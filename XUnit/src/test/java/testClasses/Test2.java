package testClasses;

import ru.java.xunit.annotations.Test;

public class Test2 {
    @Test
    public void test1() {}

    @Test
    public void test2() {}

    @Test
    public void test3() {}

    @Test
    public void test4() {}

    @Test
    public void test5() {}

    public void notTest() {}

    @Test(ignore = "ignore")
    public void ignoredTest() {}
}
