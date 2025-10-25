package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.DBConnection;


import java.io.IOException;

@WebServlet("/userregister")
public class UserRegisterServlet extends HttpServlet {

    final static Logger logger = LogManager.getLogger(UserCheckServlet.class);
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserRepository repository = new UserRepository();
        logger.debug("done-1");
        if (session == null || session.getAttribute("user") == null) {
            String login = request.getParameter("username");
            String password1 = request.getParameter("password1");
            String password2 = request.getParameter("password2");
            logger.debug("done0");
            if (repository.findByLogin(login) && password1.equals(password2)) {
                User user = new User(login, password2);
                logger.debug("done1");
                if (repository.save(user)) {
                    logger.debug("done2");
                    session = request.getSession(true);
                    session.setAttribute("user", login);
                    response.sendRedirect(request.getContextPath() + "/index");
                } else {
                    request.setAttribute("errormessage", "Карамба - не удалось зарегистрироваться");
                    request.getRequestDispatcher("/registration.ftlh")
                            .forward(request, response);
                }
            } else {
                request.setAttribute("errormessage", "Карамба - логин занят или пароль не совпадает");
                request.getRequestDispatcher("/registration.ftlh")
                        .forward(request, response);
            }
        }

    }
}
