package org.example.spring_context.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class DispatcherServlet extends HttpServlet {

    private Context applicationContext;

    public DispatcherServlet(Context context) {
        this.applicationContext = context;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        System.out.println("Получен запрос: " + req.getMethod() + " " + path);
        HandlerMethod handler = applicationContext.getHandlerForPath(path);

        if (handler != null) {
            Object controllerInstance = handler.getControllerInstance();
            Method method = handler.getMethod();
            try {
                method.invoke(controllerInstance, req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("text/html; charset=utf-8");
            PrintWriter writer = resp.getWriter();
            writer.println("<html><head><meta charset='utf-8'/><title>404</title></head><body>");
            writer.println("<h1>Ресурс не найден: " + path + "</h1>");
            writer.println("</body></html>");
        }
    }
}

