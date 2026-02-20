package org.example.tomcat;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.example.config.Context;
import org.example.config.DispatcherServlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class TestEmbeddedTomcat {
    public static void main(String[] args) {
        Context applicationContext = new Context();

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        Connector conn = new Connector();
        conn.setPort(8090);
        tomcat.setConnector(conn);

        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();
        org.apache.catalina.Context tomcatContext = tomcat.addWebapp(contextPath, docBase);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

        String servletName = "dispatcherServlet";
        tomcat.addServlet(contextPath, servletName, dispatcherServlet);
        tomcatContext.addServletMappingDecoded("/*", servletName);

        try {
            tomcat.start();
            System.out.println("Tomcat started on port " + conn.getPort());
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            System.err.println("Ошибка при запуске Tomcat:");
            e.printStackTrace();
        }

    }
}
//Реализация веб-приложения встроенный tomcat 1 сервлет - диспетчер
// 1) определить ресурс (/home, /index)
// 2) найти метод, обслуживающий этот ресурс (методы достаточно сделать унифицированными с двумя аргументами: HttpServletRequest req, HttpServletResponse resp)
// 3) методы контроллера аннотировать как @GetMapping("/index/") 4) вызов нужного метода

//
//задача контекста:
// 1) найти и проинициализировать компоненты (@Component) и контроллеры (@Controller)
// 2) для контроллеров выявить методы @GetMapping, и сохранить их в структуру Map<String, Method> - <HTTP ресурс; метод контроллера, обрабатывающий ресурс>
//Запуск приложения как в примере
//2 класса контроллера
//статические страницы