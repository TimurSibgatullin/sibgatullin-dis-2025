package org.example.config;

import org.example.annotition.Controller;
import org.example.annotition.GetMapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
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

        // создание экземплярова компонент
        // перебираем список классов
        // находим конструктор с аргументами, если такого нет - создаем экземпляр
        // размещаем в компонентс, удаляем их списка
        // если есть конструктор с аргументами (только компоненты)
        // пытаемся получить из компонентс объекты - аргументы
        // если полный набор - создаем экземпляр, иначе пропускаем итерацию
        int countClasses = classes.size();
        while (countClasses > 0) {
            // перебираем классы компонентов
            objectNotFound:
            for (Class c : classes) {
                if (components.get(c) != null) continue;
                // берём первый попавшийся
                Constructor constructor = c.getConstructors()[0];
                // извлекаем типы аргументов коструктора
                Class[] types = constructor.getParameterTypes();
                // пытаемся найти готовые компоненты по аргументу
                Object[] args = new Object[types.length];
                for (int i = 0; i < types.length; i++) {
                    args[i] = components.get(types[i]);
                    if (args[i] == null) {
                        continue objectNotFound;
                    }
                }
                try {
                    Object o = constructor.newInstance(args);
                    components.put(c, o);
                    countClasses--;
                    System.out.println(c +  " добавлено");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
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
