package com.pelgray.otus;

import com.pelgray.otus.logging.LoggingInvocationHandler;
import com.pelgray.otus.logging.test.TestLogging;
import com.pelgray.otus.logging.test.TestLoggingInterface;

public class Runner {
    public static void main(String[] args) {
        var testLogging = LoggingInvocationHandler.getLoggedInstance(new TestLogging(), TestLoggingInterface.class);
        new Demo().action(testLogging);
    }
}
