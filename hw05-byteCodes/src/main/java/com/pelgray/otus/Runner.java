package com.pelgray.otus;

import com.pelgray.otus.logging.LoggingInvocationHandler;
import com.pelgray.otus.logging.test.TestLogging;
import com.pelgray.otus.logging.test.TestLoggingInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Runner {
    public static void main(String[] args) {
        var handler = new LoggingInvocationHandler(new TestLogging());
        var testLogging = getProxyInstance(handler, TestLoggingInterface.class);
        new Demo().action(testLogging);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getProxyInstance(InvocationHandler handler, Class<T> interfaceClazz) {
        return (T) Proxy.newProxyInstance(Runner.class.getClassLoader(), new Class<?>[]{interfaceClazz}, handler);
    }

}
