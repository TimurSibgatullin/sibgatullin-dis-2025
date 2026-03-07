package org.example.config;

import org.example.annotition.Controller;
import org.example.annotition.GetMapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {

    private String scanPath = "org.example";
    private Map<Class<?>, Object> components = new HashMap<>();

    private Map<String, HandlerMethod> requestMappings = new HashMap<>();

    public Context() {
        scanComponent();
        registerRequestHandlers();
    }

    public Object getComponent(Class clazz) {
        return components.get(clazz);
    }

    public HandlerMethod getHandlerForPath(String path) {
        return requestMappings.get(path);
    }

    private void scanComponent() {
        List<Class<?>> classes = PathScan.find(scanPath);

        int countClasses = classes.size();
        int lastCount;
        do {
            lastCount = countClasses;
            for (Class<?> c : classes) {
                if (components.containsKey(c)) {
                    continue;
                }

                Constructor<?>[] constructors = c.getConstructors();
                if (constructors.length == 0) {
                    continue;
                }

                Constructor<?> candidateConstructor = constructors[0];
                Class<?>[] paramTypes = candidateConstructor.getParameterTypes();

                Object[] args = new Object[paramTypes.length];
                boolean canInstantiate = true;
                for (int i = 0; i < paramTypes.length; i++) {
                    Object dependency = components.get(paramTypes[i]);
                    if (dependency == null) {
                        canInstantiate = false;
                        break;
                    }
                    args[i] = dependency;
                }

                if (canInstantiate) {
                    try {
                        Object instance = candidateConstructor.newInstance(args);
                        components.put(c, instance);
                        countClasses--;
                        System.out.println("Компонент " + c.getSimpleName() + " создан и добавлен.");
                    } catch (Exception e) {
                        System.err.println("Ошибка при создании компонента " + c.getSimpleName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } while (countClasses > 0 && countClasses < lastCount);

        if (countClasses > 0) {
            System.err.println("Не удалось создать все компоненты");
        }
    }


    private void registerRequestHandlers() {
        for (Map.Entry<Class<?>, Object> entry : components.entrySet()) {
            Class<?> beanType = entry.getKey();
            Object beanInstance = entry.getValue();
            if (beanType.isAnnotationPresent(Controller.class)) {
                Method[] methods = beanType.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        GetMapping mappingAnnotation = method.getAnnotation(GetMapping.class);
                        String path = mappingAnnotation.value();
                        HandlerMethod handler = new HandlerMethod(beanInstance, method);
                        requestMappings.put(path, handler);
                        System.out.println("Зарегистрирован обработчик GET для '" + path + "' -> " + method);
                    }
                }
            }
        }
    }
}
