package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.User;
import org.example.repository.UserRepository;

import java.io.IOException;

@WebServlet("/showone")
public class ShowoneServlet extends HttpServlet {
    final static Logger logger = LogManager.getLogger(ShowoneServlet.class);
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        request.setAttribute("current_context", request.getContextPath());
        UserRepository repository = new UserRepository();
        User user = repository.findById(id);
        request.setAttribute("id", id);
        request.setAttribute("login", user.getLogin());
        request.setAttribute("role", user.getLogin());
        request.setAttribute("firstname", user.getLogin());
        request.setAttribute("lastname", user.getLogin());
        request.setAttribute("phone", user.getLogin());
        request.getRequestDispatcher("/showone.ftlh")
                .forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserRepository repository = new UserRepository();
        Integer id = Integer.parseInt(request.getParameter("id"));
        String login = request.getParameter("login");
        String role = request.getParameter("role");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String phone = request.getParameter("phone");

        User user = new User(id, login, role, firstname, lastname, phone);
        repository.update(user);
        response.sendRedirect(request.getContextPath() + "/show");
    }
}

