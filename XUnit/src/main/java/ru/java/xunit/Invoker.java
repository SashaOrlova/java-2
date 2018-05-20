package ru.java.xunit;

import org.jetbrains.annotations.NotNull;
import ru.java.xunit.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Invoker {
    public static void getClasses(@NotNull Class clazz) {
        ArrayList<Method> TestMethods = new ArrayList<>();
        ArrayList<Method> BeforeMethods = new ArrayList<>();
        ArrayList<Method> AfterMethods = new ArrayList<>();
        ArrayList<Method> BeforeClassMethods = new ArrayList<>();
        ArrayList<Method> AfterClassMethods = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                TestMethods.add(method);
            }
            if (method.getAnnotation(Before.class) != null) {
                BeforeMethods.add(method);
            }
            if (method.getAnnotation(After.class) != null) {
                AfterMethods.add(method);
            }
            if (method.getAnnotation(BeforeClass.class) != null) {
                BeforeClassMethods.add(method);
            }
            if (method.getAnnotation(AfterClass.class) != null) {
                AfterClassMethods.add(method);
            }
        }
    }
}
