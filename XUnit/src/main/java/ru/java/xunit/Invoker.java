package ru.java.xunit;

import org.jetbrains.annotations.NotNull;
import ru.java.xunit.annotations.*;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Invoker {
    private static class MethodsKeeper {
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
        Object instance = null;
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

    public static void invoke(@NotNull MethodsKeeper methodsKeeper, Writer writer) throws InvokerException, IOException {
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
                        writer.write("Exception not throw");
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
        writer.write("ALL TESTS: " + Integer.toString(countTests));
        writer.write("\nTESTS PASSED: " + Integer.toString(passedTests));
        writer.write("\nTIME: " + Long.toString(finishTime - startTime));
    }

    static class InvokerException extends Exception {
        InvokerException(String message) {
            super(message);
        }
    }
}
