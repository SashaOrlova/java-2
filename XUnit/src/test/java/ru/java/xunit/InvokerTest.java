package ru.java.xunit;
import org.junit.Test;
import testClasses.Test1;
import testClasses.Test2;
import testClasses.Test3;
import testClasses.Test4;

import java.io.StringWriter;
import static org.junit.Assert.*;

public class InvokerTest {
    @Test
    public void smokeTest() throws Exception {
        StringWriter wr = new StringWriter();
        Invoker.invoke(Invoker.getMethods(Test1.class), wr);
        String ans = wr.toString();
        String[] res = ans.split("\n");
        assertEquals("ALL TESTS: 1", res[0]);
        assertEquals("TESTS PASSED: 1", res[1]);
    }

    @Test
    public void manyTests() throws Exception {
        StringWriter wr = new StringWriter();
        Invoker.invoke(Invoker.getMethods(Test2.class), wr);
        String ans = wr.toString();
        String[] res = ans.split("\n");
        assertEquals("ALL TESTS: 5", res[0]);
        assertEquals("TESTS PASSED: 5", res[1]);
    }

    @Test
    public void ignoredAndExpected() throws Exception {
        StringWriter wr = new StringWriter();
        Invoker.invoke(Invoker.getMethods(Test3.class), wr);
        String ans = wr.toString();
        String[] res = ans.split("\n");
        assertEquals("ALL TESTS: 3", res[0]);
        assertEquals("TESTS PASSED: 3", res[1]);
    }

    @Test
    public void failTests() throws Exception {
        StringWriter wr = new StringWriter();
        Invoker.invoke(Invoker.getMethods(Test4.class), wr);
        String ans = wr.toString();
        String[] res = ans.split("\n");
        assertEquals("ALL TESTS: 3", res[0]);
        assertEquals("TESTS PASSED: 1", res[1]);
    }
}