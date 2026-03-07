package org.example.spring_context.component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;


@Controller
public class IndexController {
    public IndexController() {
    }

    @GetMapping("/index")
    public void homePage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        writer.println("<html><head><meta charset='utf-8'/><title>INDEX</title></head><body>");
        writer.println("<h1>Добро пожаловать на главную страницу!</h1>");
        writer.println("<p>Это главная страница, обслуживаемая IndexController.</p>");
        writer.println("</body></html>");
    }
}