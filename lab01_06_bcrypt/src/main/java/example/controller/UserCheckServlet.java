package example.controller;

import example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

@WebServlet("/usercheck")
public class UserCheckServlet extends HttpServlet {

    final static Logger logger = LogManager.getLogger(UserCheckServlet.class);
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserRepository repository = new UserRepository();
        HttpSession session = request.getSession(false);
        String resourse = "/index.ftlh";
        if (session == null || session.getAttribute("user") == null) {
            String login = request.getParameter("username");
            String password = request.getParameter("password");
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            logger.debug(repository.checkPassword(login));
            if (bCryptPasswordEncoder.matches(password, repository.checkPassword(login))) {
                session = request.getSession(true);
                session.setAttribute("user", login);

                User user = repository.getUserByLogin(login);
                session.setAttribute("role", user.getRole());
                session.setAttribute("firstname", user.getFirstname());
                session.setAttribute("lastname", user.getLastname());
                session.setAttribute("phone", user.getPhone());
                resourse = "/index.ftlh";
            } else {
                request.setAttribute("errormessage", "Карамба - неверный логин или пароль");
                resourse = "/login.ftlh";
            }
        }
        request.getRequestDispatcher(resourse)
                .forward(request, response);
    }

}