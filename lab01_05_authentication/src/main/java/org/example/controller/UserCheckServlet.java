package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;

@WebServlet("/usercheck")
public class UserCheckServlet extends HttpServlet {

    final static Logger logger = LogManager.getLogger(UserCheckServlet.class);
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String resourse = "/index.ftlh";
        if (session == null || session.getAttribute("user") == null) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (username.equals("admin") && password.equals("admin")) {
                session = request.getSession(true);
                session.setAttribute("user", username);
                resourse = "/index.ftlh";
            } else {
                request.setAttribute("errormessage", "Карамба");
                resourse = "/login.ftlh";
            }
        }
        request.getRequestDispatcher(resourse)
                .forward(request, response);
    }

}