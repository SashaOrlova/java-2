package testClasses;

import ru.java.xunit.annotations.*;

public class Test5 {
    public static final StringBuilder answer = new StringBuilder();

    @BeforeClass
    public void beforeClass() {
        answer.append("BeforeClass ");
    }

    @Before
    public void before() {
        answer.append("Before ");
    }

    @Test
    public void test() {
        answer.append("Test ");
    }

    @After
    public void after() {
        answer.append("After ");
    }

    @AfterClass
    public void afterClass() {
        answer.append("AfterClass");
    }
}
