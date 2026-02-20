package org.example.config;

import java.lang.reflect.Method;

public class HandlerMethod {
    private final Object controllerInstance;
    private final Method method;

    public HandlerMethod(Object controllerInstance, Method method) {
        this.controllerInstance = controllerInstance;
        this.method = method;
    }

    public Object getControllerInstance() {
        return controllerInstance;
    }

    public Method getMethod() {
        return method;
    }
}
