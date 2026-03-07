package org.example.spring_context.config;

import org.springframework.stereotype.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Context {

    private ApplicationContext springContext;
    private Map<String, HandlerMethod> requestMappings = new HashMap<>();

    public Context() {
        springContext = new AnnotationConfigApplicationContext("org.example.spring_context.component");
        registerRequestHandlers();

    }

    public Object getComponent(Class clazz) {
        return springContext.getBean(clazz);
    }

    public HandlerMethod getHandlerForPath(String path) {
        return requestMappings.get(path);
    }

    private void registerRequestHandlers() {
        Map<String, Object> controllers =
                springContext.getBeansWithAnnotation(Controller.class);

        for (Object bean : controllers.values()) {
            Class<?> beanType = bean.getClass();
            for (Method method : beanType.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping mapping = method.getAnnotation(GetMapping.class);
                    for (String path : mapping.value()) {
                        HandlerMethod handler = new HandlerMethod(bean, method);
                        requestMappings.put(path, handler);

                        System.out.println("GET " + path + " -> " + method);
                    }
                }
            }
        }
    }
}
