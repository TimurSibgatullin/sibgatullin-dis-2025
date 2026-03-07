package org.example;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.example.config.Context;
import org.example.config.DispatcherServlet;

import java.io.File;

public class TestPathScan {
    public static void main(String[] args) {
        Context applicationContext = new Context();

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        Connector conn = new Connector();
        conn.setPort(8090);
        tomcat.setConnector(conn);

        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();
        org.apache.catalina.Context tomcatContext = tomcat.addContext(contextPath, docBase);

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
