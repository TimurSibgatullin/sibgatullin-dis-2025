package org.example.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Context {
    private String scanPath = "org.example";

    public Context() {
        scanComponent();
    }

    private Map<Class<?>, Object> components = new HashMap<>();
    public Object getComponent(Class clazz) {
       return components.get(clazz);
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
}
