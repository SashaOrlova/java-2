package ru.java.xunit;

import org.jetbrains.annotations.NotNull;
import ru.java.xunit.annotations.*;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Invoker {
    private static class MethodsKeeper {
        ArrayList<Method> testMethods;
        ArrayList<Method> beforeMethods;
        ArrayList<Method> afterMethods;
        ArrayList<Method> beforeClassMethods;
        ArrayList<Method> afterClassMethods;
        MethodsKeeper(ArrayList<Method> testMethods, ArrayList<Method> beforeMethods, ArrayList<Method> afterMethods,
                      ArrayList<Method> beforeClassMethods, ArrayList<Method> afterClassMethods) {
            this.testMethods = testMethods;
            this.beforeMethods = beforeMethods;
            this.afterMethods = afterMethods;
            this.beforeClassMethods = beforeClassMethods;
            this.afterClassMethods = afterClassMethods;
        }
    }

    @NotNull
    public static MethodsKeeper getMethods(@NotNull Class clazz) {
        ArrayList<Method> testMethods = new ArrayList<>();
        ArrayList<Method> beforeMethods = new ArrayList<>();
        ArrayList<Method> afterMethods = new ArrayList<>();
        ArrayList<Method> beforeClassMethods = new ArrayList<>();
        ArrayList<Method> afterClassMethods = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                testMethods.add(method);
            }
            if (method.getAnnotation(Before.class) != null) {
                beforeMethods.add(method);
            }
            if (method.getAnnotation(After.class) != null) {
                afterMethods.add(method);
            }
            if (method.getAnnotation(BeforeClass.class) != null) {
                beforeClassMethods.add(method);
            }
            if (method.getAnnotation(AfterClass.class) != null) {
                afterClassMethods.add(method);
            }
        }
        return new MethodsKeeper(testMethods, beforeMethods, afterMethods, beforeClassMethods, afterClassMethods);
    }

    public static void invokeClass(@NotNull MethodsKeeper methodsKeeper, Writer writer) {
        int passed = 0;
        int succ = 0;
        int countTests = 0;
        long startTime = System.currentTimeMillis();
        try {

            try {
                for (Method before : methodsKeeper.beforeClassMethods) {
                    before.invoke(null);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.out.println(e.getMessage());
            }

            try {
                for (Method test : methodsKeeper.testMethods) {
                    for (Method before : methodsKeeper.beforeMethods) {
                        before.invoke(null);
                    }
                    try {
                        if (test.getAnnotation(Test.class).ignore().equals("")) {
                            test.invoke(null);
                            countTests++;
                        }
                        else {
                            countTests--;
                        }
                        if (test.getAnnotation(Test.class).expected().equals(Test.EmptyException.class)) {
                           throw new InvokerException();
                        }
                    } catch (Exception e) {
                        if (!test.getAnnotation(Test.class).expected().equals(e.getClass())) {
                            throw e;
                        }
                    }
                    for (Method after : methodsKeeper.afterMethods) {
                        after.invoke(null);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                succ--;
                System.out.println(e.getMessage());

            }

            try {
                for (Method after : methodsKeeper.afterClassMethods) {
                    after.invoke(null);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.out.println(e.getMessage());
            }
        }
        catch (Exception e) {
            succ--;
        }
        try {
            writer.write("TESTS: ");
            writer.write(countTests);
            writer.write("\nTEST PASSED: ");
            writer.write(succ);
            long endTime = System.currentTimeMillis();
            writer.write("\nTIME: ");
            writer.write((int) (endTime - startTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class InvokerException extends Exception {}
}
