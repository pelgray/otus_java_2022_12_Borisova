package com.pelgray.otus.logging;

import com.pelgray.otus.logging.annotation.Log;
import com.pelgray.otus.logging.test.TestLoggingInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoggingInvocationHandler implements InvocationHandler {
    private final TestLoggingInterface loggingClass;

    public LoggingInvocationHandler(TestLoggingInterface loggingInterface) {
        this.loggingClass = loggingInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isLogEnabled(method)) {
            var info = new StringBuilder("\n==> called method \"").append(method.getName()).append("\"");
            var methodParameters = method.getParameters();
            for (int i = 0; i < methodParameters.length; i++) {
                info.append(", ").append(methodParameters[i].toString()).append("=").append(args[i]);
            }
            if (methodParameters.length == 0) {
                info.append(", with no params");
            }
            info.append("\n");
            System.out.println(info);
        }
        return method.invoke(loggingClass, args);
    }

    private boolean isLogEnabled(Method method) throws NoSuchMethodException {
        return loggingClass.getClass()
                .getDeclaredMethod(method.getName(), method.getParameterTypes())
                .isAnnotationPresent(Log.class);
    }

    @Override
    public String toString() {
        return "LoggingInvocationHandler{" +
                "logging=" + loggingClass +
                '}';
    }
}
