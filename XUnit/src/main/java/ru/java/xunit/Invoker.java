package ru.java.xunit;

import org.jetbrains.annotations.NotNull;
import ru.java.xunit.annotations.*;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Class for invoke test classes and call test methods
 */
public class Invoker {
    private static ArrayList<Class<? extends Annotation>> annotations = new ArrayList<>();

    static {
        annotations.add(Test.class);
        annotations.add(Before.class);
        annotations.add(After.class);
        annotations.add(BeforeClass.class);
        annotations.add(AfterClass.class);
    }

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
        void setBeforeClassMethod(Method method) throws InvokerInvocationTargetException {
            if (beforeClassMethod != null)
                throw new InvokerInvocationTargetException("More the one BeforeClass annotation");
            beforeClassMethod = method;
        }
        void setAfterClassMethod(Method method) throws InvokerInvocationTargetException {
            if (afterClassMethod != null)
                throw new InvokerInvocationTargetException("More the one AfterClass annotation");
            afterClassMethod = method;
        }
        Method getBeforeClassMethod() {
            return beforeClassMethod;
        }
        Method getAfterClassMethod() {
            return afterClassMethod;
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

    private static <T extends Annotation> boolean checkAnnotation(@NotNull Method method, @NotNull Class<T> clazz)
            throws InvokerCreateException {
        if (method.getAnnotation(clazz) != null) {
            for (Class<? extends Annotation> annotation : annotations) {
                if (!annotation.equals(clazz) && method.getAnnotation(annotation) != null) {
                    throw new InvokerCreateException("Two annotations for one method");
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @param clazz - class for testing
     * @return MethodKeeper stored methods of class
     * @throws InvokerInvocationTargetException - if class not suitable for testing
     */
    @NotNull
    public static MethodsKeeper getMethods(@NotNull Class clazz)
            throws InvokerInvocationTargetException, InvokerCreateException {
        MethodsKeeper methodsKeeper = new MethodsKeeper();
        methodsKeeper.setClazz(clazz);
        for (Method method : clazz.getMethods()) {
            if (checkAnnotation(method, Test.class)) {
                methodsKeeper.addTestMethod(method);
            }
            if (checkAnnotation(method, Before.class)) {
                methodsKeeper.addBeforeMethod(method);
            }
            if (checkAnnotation(method, After.class)) {
                methodsKeeper.addAfterMethod(method);
            }
            if (checkAnnotation(method, BeforeClass.class)) {
                methodsKeeper.setBeforeClassMethod(method);
            }
            if (checkAnnotation(method, AfterClass.class)) {
                methodsKeeper.setAfterClassMethod(method);
            }
        }
        return methodsKeeper;
    }

    @NotNull
    private static Object getInstance(@NotNull Class clazz) throws InvokerCreateException, InvokerIllegalAccessException {
        Object instance;
        try {
            Constructor<?> constructor;
            constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            throw new InvokerCreateException("Exception in create class");
        } catch (IllegalAccessException e) {
            throw new InvokerIllegalAccessException(e.getMessage());
        }
        return instance;
    }

    /**
     * invoke class represented in methodKeeper
     * @param methodsKeeper methodsKeeper stored all methods of class
     * @param writer writer where result of test be write
     * @throws InvokerInvocationTargetException if class not suitable for testing
     * @throws IOException if happens exception with writer
     */
    public static void invoke(@NotNull MethodsKeeper methodsKeeper, @NotNull PrintStream writer)
            throws InvokerInvocationTargetException, IOException, InvokerCreateException, InvokerIllegalAccessException {
        int passedTests = 0;
        int countTests = 0;
        Object instance = getInstance(methodsKeeper.getClazz());
        long startTime = System.currentTimeMillis();
        if (methodsKeeper.getBeforeClassMethod() != null) {
            try {
                methodsKeeper.getBeforeClassMethod().invoke(instance);
            } catch (InvocationTargetException e) {
                throw new InvokerInvocationTargetException("Exception in invoke beforeClass method");
            } catch (IllegalAccessException e) {
                throw new InvokerIllegalAccessException(e.getMessage());
            }
        }
        for (Method testMethod : methodsKeeper.getTestMethods()) {
            for (Method beforeTest : methodsKeeper.getBeforeMethods()) {
                try {
                    beforeTest.invoke(instance);
                } catch (InvocationTargetException e) {
                    throw new InvokerInvocationTargetException("Exception in invoke beforeTest method");
                }   catch (IllegalAccessException e) {
                    throw new InvokerIllegalAccessException(e.getMessage());
                }
            }
            if (testMethod.getAnnotation(Test.class).ignore().equals("")) {
                countTests++;
                try {
                    testMethod.invoke(instance);
                    if (!testMethod.getAnnotation(Test.class).expected().equals(Test.EmptyException.class)) {
                        writer.println("Exception not throw at test " + testMethod.getName());
                    }
                    passedTests++;
                } catch (InvocationTargetException e) {
                    if (testMethod.getAnnotation(Test.class).expected().equals(e.getCause().getClass())) {
                        passedTests++;
                    }
                } catch (IllegalAccessException e) {
                    throw new InvokerIllegalAccessException(e.getMessage());
                }
            } else {
                writer.println(testMethod.getAnnotation(Test.class).ignore());
            }
            for (Method afterTest : methodsKeeper.getAfterMethods()) {
                try {
                    afterTest.invoke(instance);
                } catch (InvocationTargetException e) {
                    throw new InvokerInvocationTargetException("Exception in invoke afterTest method");
                }  catch (IllegalAccessException e) {
                    throw new InvokerIllegalAccessException(e.getMessage());
                }
            }
        }
        if (methodsKeeper.getAfterClassMethod() != null) {
            try {
                methodsKeeper.getAfterClassMethod().invoke(instance);
            } catch (InvocationTargetException e) {
                throw new InvokerInvocationTargetException("Exception in invoke beforeClass method");
            } catch (IllegalAccessException e) {
                throw new InvokerIllegalAccessException(e.getMessage());
            }
        }
        long finishTime = System.currentTimeMillis();
        writer.println("ALL TESTS: " + Integer.toString(countTests));
        writer.println("TESTS PASSED: " + Integer.toString(passedTests));
        writer.println("TIME: " + Long.toString(finishTime - startTime));
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                Class testClass = Class.forName(args[0]);
                invoke(getMethods(testClass), System.out);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found!");
            } catch (InvokerInvocationTargetException | InvokerCreateException | InvokerIllegalAccessException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Problem with output");
            }
        }
    }

    static class InvokerInvocationTargetException extends Exception {
        InvokerInvocationTargetException(String message) {
            super(message);
        }
    }
    static class InvokerCreateException extends Exception {
        InvokerCreateException(String message) {
            super(message);
        }
    }
    static class InvokerIllegalAccessException extends Exception {
        InvokerIllegalAccessException(String message) {
            super(message);
        }
    }
}
