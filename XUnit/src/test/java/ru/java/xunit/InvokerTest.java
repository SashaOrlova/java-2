package ru.java.xunit;
import org.junit.Test;
import testClasses.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

public class InvokerTest {
    @Test
    public void smokeTest() throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteStream);
        Invoker.invoke(Invoker.getMethods(Test1.class), printStream);
        String ans = byteStream.toString();
        String[] res = ans.split(System.lineSeparator());
        assertEquals("ALL TESTS: 1", res[0]);
        assertEquals("TESTS PASSED: 1", res[1]);
    }

    @Test
    public void manyTests() throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteStream);
        Invoker.invoke(Invoker.getMethods(Test2.class), printStream);
        String ans = byteStream.toString();
        String[] res = ans.split(System.lineSeparator());
        assertEquals("ignore", res[0]);
        assertEquals("ALL TESTS: 5", res[1]);
        assertEquals("TESTS PASSED: 5", res[2]);
    }

    @Test
    public void ignoredAndExpected() throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteStream);
        Invoker.invoke(Invoker.getMethods(Test3.class), printStream);
        String ans = byteStream.toString();
        String[] res = ans.split(System.lineSeparator());
        assertEquals("ignored", res[0]);
        assertEquals("ignored", res[1]);
        assertEquals("ignored", res[2]);
        assertEquals("ALL TESTS: 3", res[3]);
        assertEquals("TESTS PASSED: 3", res[4]);
    }

    @Test
    public void failTests() throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteStream);
        Invoker.invoke(Invoker.getMethods(Test4.class), printStream);
        String ans = byteStream.toString();
        String[] res = ans.split(System.lineSeparator());
        assertEquals("not a test", res[0]);
        assertEquals("ALL TESTS: 3", res[1]);
        assertEquals("TESTS PASSED: 1", res[2]);
    }

    @Test
    public void testSequence() throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteStream);
        Invoker.invoke(Invoker.getMethods(Test5.class), printStream);
        String ans = byteStream.toString();
        String[] res = ans.split(System.lineSeparator());
        assertEquals("ALL TESTS: 1", res[0]);
        assertEquals("BeforeClass Before Test After AfterClass", Test5.answer.toString());
    }
}