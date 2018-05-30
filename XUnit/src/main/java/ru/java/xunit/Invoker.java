package ru.java.xunit;

import org.jetbrains.annotations.NotNull;
import ru.java.xunit.annotations.*;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Class for invoke test classes and call test methods
 */
public class Invoker {
    /**
     * Class for store methods of test class
     */
    public static class MethodsKeeper {
        ArrayList<Method> testMethods = new ArrayList<>();
        ArrayList<Method> beforeMethods = new ArrayList<>();
        ArrayList<Method> afterMethods = new ArrayList<>();
        Method beforeClassMethod;
        Method afterClassMethod;
        Class<?> clazz;

        void addTestMethod(Method method) {
            testMethods.add(method);
        }
        void addBeforeMethod(Method method) {
            beforeMethods.add(method);
        }
        void addAfterMethod(Method method) {
            afterMethods.add(method);
        }
        void setBeforeClassMethod(Method method) throws InvokerException {
            if (beforeClassMethod != null)
                throw new InvokerException("More the one BeforeClass annotations");
            beforeClassMethod = method;
        }
        void setAfterClassMethod(Method method) throws InvokerException {
            if (afterClassMethod != null)
                throw new InvokerException("More the one AfterClass annotations");
            afterClassMethod = method;
        }
        Method getBeforeClassMethod() {
            return beforeClassMethod;
        }
        ArrayList<Method> getTestMethods() {
            return testMethods;
        }
        ArrayList<Method> getBeforeMethods() {
            return beforeMethods;
        }
        ArrayList<Method> getAfterMethods() {
            return afterMethods;
        }
        void setClazz(Class clazz) {
            this.clazz = clazz;
        }
        Class getClazz() {
            return clazz;
        }
    }

    /**
     * @param clazz - class for testing
     * @return MethodKeeper stored methods of class
     * @throws InvokerException - if class not suitable for testing
     */
    @NotNull
    public static MethodsKeeper getMethods(@NotNull Class clazz) throws InvokerException {
        MethodsKeeper methodsKeeper = new MethodsKeeper();
        methodsKeeper.setClazz(clazz);
        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                methodsKeeper.addTestMethod(method);
            }
            if (method.getAnnotation(Before.class) != null) {
                methodsKeeper.addBeforeMethod(method);
            }
            if (method.getAnnotation(After.class) != null) {
                methodsKeeper.addAfterMethod(method);
            }
            if (method.getAnnotation(BeforeClass.class) != null) {
                methodsKeeper.setBeforeClassMethod(method);
            }
            if (method.getAnnotation(AfterClass.class) != null) {
                methodsKeeper.setAfterClassMethod(method);
            }
        }
        return methodsKeeper;
    }

    private static Object getInstance(Class clazz) throws InvokerException {
        Object instance;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new InvokerException("Exception in create class");
        }
        return instance;
    }

    /**
     * invoke class represented in methodKeeper
     * @param methodsKeeper methodsKeeper stored all methods of class
     * @param writer writer where result of test be write
     * @throws InvokerException if class not suitable for testing
     * @throws IOException if happens exception with writer
     */
    public static void invoke(@NotNull MethodsKeeper methodsKeeper, PrintStream writer) throws InvokerException, IOException {
        int passedTests = 0;
        int countTests = 0;
        Object instance = getInstance(methodsKeeper.getClazz());
        long startTime = System.currentTimeMillis();
        if (methodsKeeper.getBeforeClassMethod() != null) {
            try {
                methodsKeeper.getBeforeClassMethod().invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InvokerException("Exception in invoke");
            }
        }
        for (Method testMethod : methodsKeeper.getTestMethods()) {
            for (Method beforeTest : methodsKeeper.getBeforeMethods()) {
                try {
                    beforeTest.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InvokerException("Exception in invoke");
                }
            }
            if (testMethod.getAnnotation(Test.class).ignore().equals("")) {
                countTests++;
                try {
                    testMethod.invoke(instance);
                    if (!testMethod.getAnnotation(Test.class).expected().equals(Test.EmptyException.class)) {
                        writer.println("Exception not throw");
                    }
                    passedTests++;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    if (testMethod.getAnnotation(Test.class).expected().equals(e.getCause().getClass())) {
                        passedTests++;
                    }
                }
            }
            for (Method afterTest : methodsKeeper.getAfterMethods()) {
                try {
                    afterTest.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InvokerException("Exception in invoke\n");
                }
            }
        }
        long finishTime = System.currentTimeMillis();
        writer.println("ALL TESTS: " + Integer.toString(countTests));
        writer.println("TESTS PASSED: " + Integer.toString(passedTests));
        writer.println("\nTIME: " + Long.toString(finishTime - startTime));
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                Class testClass = Class.forName(args[0]);
                invoke(getMethods(testClass), System.out);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found!");
            } catch (InvokerException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Problem with output");
            }
        }
    }

    static class InvokerException extends Exception {
        InvokerException(String message) {
            super(message);
        }
    }
}
