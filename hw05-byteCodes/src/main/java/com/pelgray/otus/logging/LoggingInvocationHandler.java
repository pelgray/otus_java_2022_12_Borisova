package com.pelgray.otus.logging;

import com.pelgray.otus.logging.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class LoggingInvocationHandler implements InvocationHandler {
    private final Object targetObject;

    private final Map<Method, LogInfo> cachedLogInfo = new HashMap<>();

    private LoggingInvocationHandler(Object targetObject) {
        this.targetObject = targetObject;
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    public static <T extends I, I> I getLoggedInstance(T targetObject, Class<I> interfaceClazz) {
        var handler = new LoggingInvocationHandler(targetObject);
        return (I) Proxy.newProxyInstance(LoggingInvocationHandler.class.getClassLoader(),
                                          new Class<?>[]{interfaceClazz}, handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        getLogInfo(method).print(args);
        return method.invoke(targetObject, args);
    }

    private LogInfo getLogInfo(Method method) throws NoSuchMethodException {
        if (cachedLogInfo.containsKey(method)) {
            return cachedLogInfo.get(method);
        }
        var isLogEnabled = targetObject.getClass()
                .getDeclaredMethod(method.getName(), method.getParameterTypes())
                .isAnnotationPresent(Log.class);

        var logInfo = isLogEnabled ? new LogInfo(getCallInfoTemplate(method)) : new LogInfo();
        cachedLogInfo.put(method, logInfo);
        return logInfo;
    }

    private String getCallInfoTemplate(Method method) {
        var info = new StringBuilder("\n==> called method \"").append(method.getName()).append("\"");
        var methodParameters = method.getParameters();
        for (var methodParameter : methodParameters) {
            info.append(", ").append(methodParameter.toString()).append("=%s");
        }
        if (methodParameters.length == 0) {
            info.append(", with no params");
        }
        info.append("\n\n");
        return info.toString();
    }

    @Override
    public String toString() {
        return "LoggingInvocationHandler{" +
                "targetObject=" + targetObject +
                '}';
    }

    static class LogInfo {
        private final boolean isEnabled;

        private final String infoTemplate;

        public LogInfo() {
            this.isEnabled = false;
            this.infoTemplate = null;
        }

        public LogInfo(String infoTemplate) {
            this.isEnabled = true;
            this.infoTemplate = infoTemplate;
        }

        public void print(Object[] args) {
            if (this.isEnabled) {
                System.out.printf(this.infoTemplate, args);
            }
        }
    }
}
