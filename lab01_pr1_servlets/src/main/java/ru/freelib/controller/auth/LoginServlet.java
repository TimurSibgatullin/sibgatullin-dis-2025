package ru.freelib.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.freelib.model.User;
import ru.freelib.service.UserService;

import java.io.IOException;

@WebServlet("/auth/usercheck")
public class LoginServlet extends HttpServlet {
    private UserService userService;
    final static Logger logger = LogManager.getLogger(LoginServlet.class);

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        request.getRequestDispatcher("/login.ftlh")
                .forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userService.authenticate(login, password);
        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            session.setAttribute("nickname", user.getNickname());
            String url = (String) request.getSession().getAttribute("redirectAfterLogin");
            if (url != null) {
                request.getSession().removeAttribute("redirectAfterLogin");
                response.sendRedirect(url);
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } else {
            request.setAttribute("errormessage", "Неверный логин или пароль");
            request.getRequestDispatcher("/login.ftlh")
                    .forward(request, response);
        }
    }
}