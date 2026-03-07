package org.example.spring_context.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class HomeController {
    @Autowired
    Application application;

    @GetMapping("/home")
    public void homePage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        writer.println("<html><head><meta charset='utf-8'/><title>HOME</title></head><body>");
        writer.println("<h1>Добро пожаловать на домашнюю страницу</h1>");
        writer.println("<p>Будь как дома путник, я ни в чём не откажу</p>");
        writer.println("</body></html>");
        application.run();
    }
}