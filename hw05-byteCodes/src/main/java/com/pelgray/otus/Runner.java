package com.pelgray.otus;

import com.pelgray.otus.logging.LoggingInvocationHandler;
import com.pelgray.otus.logging.test.TestLogging;
import com.pelgray.otus.logging.test.TestLoggingInterface;

import java.lang.reflect.Proxy;

public class Runner {
    public static void main(String[] args) {
        var testLogging = getLoggedInstance(new TestLogging(), TestLoggingInterface.class);
        new Demo().action(testLogging);
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private static <T extends I, I> I getLoggedInstance(T targetObject, Class<I> interfaceClazz) {
        var handler = new LoggingInvocationHandler(targetObject);
        return (I) Proxy.newProxyInstance(Runner.class.getClassLoader(), new Class<?>[]{interfaceClazz}, handler);
    }

}
