package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.User;
import org.example.repository.UserRepository;

import java.io.IOException;

@WebServlet("/add")
public class AddServlet extends HttpServlet {

    final static Logger logger = LogManager.getLogger(AddServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("current_context", request.getContextPath());
        request.getRequestDispatcher("/add.ftlh")
                .forward(request, response);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserRepository repository = new UserRepository();
        String login = request.getParameter("login");
        String role = request.getParameter("role");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String phone = request.getParameter("phone");

        User user = new User(login, role, firstname, lastname, phone);
        if (repository.save(user)) {
            response.sendRedirect(request.getContextPath() + "/show");
        }
    }
}