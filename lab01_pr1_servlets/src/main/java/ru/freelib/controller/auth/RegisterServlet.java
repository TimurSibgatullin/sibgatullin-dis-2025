package ru.freelib.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.freelib.model.User;
import ru.freelib.service.UserService;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/auth/userregister")
public class RegisterServlet extends HttpServlet {
    private UserService userService;
    final static Logger logger = LogManager.getLogger(RegisterServlet.class);

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        request.getRequestDispatcher("/auth/registration.ftlh")
                .forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("username");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        String role = request.getParameter("role");
        String nickname = request.getParameter("nickname");
        request.setAttribute("currentContext", request.getContextPath());
        if (Objects.equals(login, "") || Objects.equals(password1, "") || Objects.equals(password2, "") ||
                Objects.equals(nickname, "")) {
            request.setAttribute("errormessage", "Все поля должны быть заполнены");
        } else if (!password1.equals(password2)) {
            request.setAttribute("errormessage", "Пароли должны совпадать");
        } else if (!password1.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}$")) {
            request.setAttribute("errormessage", "Пароль должен быть длиньше 8 символов, иметь одну строчную, одну прописную букву и одну цифру");
        } else {
            boolean ok = userService.register(login, password1, password2, role, nickname);
            if (ok) {
                User user = userService.authenticate(login, password1);
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("role", user.getRole());
                session.setAttribute("nickname", user.getNickname());
                String url = (String) request.getSession().getAttribute("redirectAfterLogin");
                if (url != null) {
                    request.getSession().removeAttribute("redirectAfterLogin");
                    response.sendRedirect(url);
                    return;
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                    return;
                }
            } else {
                request.setAttribute("errormessage", "Логин занят");
            }
        }
        request.getRequestDispatcher("/auth/registration.ftlh")
                .forward(request, response);
    }
}