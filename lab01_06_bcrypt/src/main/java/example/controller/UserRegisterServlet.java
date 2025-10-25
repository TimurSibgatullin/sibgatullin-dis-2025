package example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import example.model.User;
import example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

@WebServlet("/userregister")
public class UserRegisterServlet extends HttpServlet {
    final static Logger logger = LogManager.getLogger(UserRegisterServlet.class);
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserRepository repository = new UserRepository();

        if (session == null || session.getAttribute("user") == null) {
            String login = request.getParameter("username");
            String password1 = request.getParameter("password1");
            String password2 = request.getParameter("password2");
            String role = request.getParameter("password2");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String phone = request.getParameter("phone");
            if (repository.findByLogin(login) && password1.equals(password2)) {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                User user = new User(login, bCryptPasswordEncoder.encode(password1), role, firstname, lastname, phone);
                if (repository.save(user)) {
                    session = request.getSession(true);
                    session.setAttribute("user", login);
                    session.setAttribute("role", user.getRole());
                    session.setAttribute("firstname", user.getFirstname());
                    session.setAttribute("lastname", user.getLastname());
                    session.setAttribute("phone", user.getPhone());
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
