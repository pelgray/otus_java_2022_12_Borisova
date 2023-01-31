package com.pelgray.otus;

import com.pelgray.otus.annotation.After;
import com.pelgray.otus.annotation.Before;
import com.pelgray.otus.annotation.Test;
import com.pelgray.otus.example.test.SimpleExampleTest;
import com.pelgray.otus.example.test.ThrowingExampleTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) throws ReflectiveOperationException {
        var testRunner = new TestRunner();
        for (var clazz : List.of(SimpleExampleTest.class, ThrowingExampleTest.class)) {
            testRunner.runTest(clazz);
        }
    }

    private void runTest(Class<?> testClazz) throws ReflectiveOperationException {
        println("Started test class: \"{0}\"\n", testClazz.getSimpleName());

        var annotationToMethodMap = getAnnotationToMethodMap(testClazz);

        var successTestCount = 0;
        var failedTestCount = 0;
        for (var test : annotationToMethodMap.get(Test.class)) {
            Object testInstance = testClazz.getConstructor().newInstance();
            var testDescription = test.getAnnotation(Test.class).value();
            try {
                invokeMethods(annotationToMethodMap, testInstance, Before.class);

                println("Test started: \"{0}\"", testDescription);
                invoke(testInstance, test);
                println("Test \"{0}\": SUCCESS", testDescription);
                successTestCount++;
            } catch (Exception ex) {
                println("Exception in test: {0}", ex.getCause());
                println("Test \"{0}\": FAILED", testDescription);
                failedTestCount++;
            } finally {
                invokeMethods(annotationToMethodMap, testInstance, After.class);
            }
            println();
        }
        println("Tests in class {0} finished: {1} failed, {2} done, total {3}\n\n", testClazz.getSimpleName(),
                successTestCount, failedTestCount, successTestCount + failedTestCount);
    }

    private HashMap<Class<? extends Annotation>, List<Method>> getAnnotationToMethodMap(Class<?> testClazz) {
        var annotationToMethodMap = new HashMap<Class<? extends Annotation>, List<Method>>();
        for (var method : testClazz.getDeclaredMethods()) {
            if (!addMethodToMap(annotationToMethodMap, Test.class, method)) {
                if (!addMethodToMap(annotationToMethodMap, Before.class, method)) {
                    addMethodToMap(annotationToMethodMap, After.class, method);
                }
            }
        }
        return annotationToMethodMap;
    }

    private boolean addMethodToMap(HashMap<Class<? extends Annotation>, List<Method>> annotationToMethodMap,
                                   Class<? extends Annotation> key, Method method) {
        if (method.isAnnotationPresent(key)) {
            annotationToMethodMap.computeIfAbsent(key, k -> new ArrayList<>());
            annotationToMethodMap.computeIfPresent(key, (k, v) -> {
                v.add(method);
                return v;
            });
            return true;
        }
        return false;
    }

    private void invoke(Object instance, Method method) throws IllegalAccessException, InvocationTargetException {
        method.setAccessible(true);
        method.invoke(instance);
    }

    private void invokeMethods(HashMap<Class<? extends Annotation>, List<Method>> annotationToMethodMap,
                               Object instance, Class<? extends Annotation> annotationKey)
            throws IllegalAccessException, InvocationTargetException {
        for (var method : annotationToMethodMap.get(annotationKey)) {
            invoke(instance, method);
        }
    }

    private void println(String pattern, Object... arguments) {
        System.out.println(MessageFormat.format(pattern, arguments));
    }

    private void println() {
        System.out.println();
    }
}
