package ru.itis.dis403;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

@WebServlet("/")
public class IndexServlet extends HttpServlet {
    public void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        String form = """
                <html>
                <body>
                <form method='post' action='/lab1_02_servlet/test'>
                    <div>
                        <input type='text' name='param1'></input>
                    </div>
                    <div>
                        <input type='submit' value='SEND'></input>
                    </div>
                </form>
                </body>
                </html>
                """;
        servletResponse.getWriter().write(form);
        new TemplateHandler().handle("index.html", Map.of(), servletResponse.getWriter());
    }

    public void doPost(HttpServletRequest servletRequest,
                       HttpServletResponse servletResponse) throws ServletException, IOException {
        doGet(servletRequest, servletResponse);
    }
}

