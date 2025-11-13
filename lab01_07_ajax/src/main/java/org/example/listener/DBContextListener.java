package org.example.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.repository.CityRepository;
import org.example.repository.StreetRepository;
import org.example.service.DBConnection;


@WebListener
public class DBContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DBConnection.init();

            CityRepository cityRepository = new CityRepository();
            StreetRepository streetRepository = new StreetRepository();

            ServletContext context = sce.getServletContext();

            context.setAttribute("cityRepository", cityRepository);
            context.setAttribute("streetRepository", streetRepository);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        DBConnection.destroy();
    }
}