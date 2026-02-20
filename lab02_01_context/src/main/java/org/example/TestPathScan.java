package org.example;

import org.example.component.Application;
import org.example.config.Context;
import org.example.config.PathScan;

import java.util.List;

public class TestPathScan {
    public static void main(String[] args) {
//        List<Class<?>> classes = PathScan.find("org.example");
//        classes.forEach(System.out::println);
        Context context = new Context();

        Application application = (Application) context.getComponent(Application.class);
        application.run();
    }
}
