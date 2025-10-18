package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.util.List;

@WebServlet("/truesession")
public class SessionServlet extends HttpServlet {

    final static Logger logger = LogManager.getLogger(TestSessionServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ищет куку
        // ищет ассоциированная с ней сессия (объект HttpSession)
        // если не нахлодится - создаётся новый (если не указан флаг false)

        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession(true);
            session.setAttribute("username", request.getParameter("user"));
            request.setAttribute("username", "инкогнито");
        } else {
            request.setAttribute("username", session.getAttribute("username"));
        }


        request.setAttribute("sessionId", session.getId());
        request.getRequestDispatcher("/test.ftlh")
                .forward(request, response);
    }

}