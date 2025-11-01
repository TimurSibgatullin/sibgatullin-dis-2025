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
import java.util.List;

@WebServlet("/show")
public class ShowServlet extends HttpServlet {
    final static Logger logger = LogManager.getLogger(ShowServlet.class);
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserRepository repository = new UserRepository();
        List<User> users = repository.findAll();
        request.setAttribute("current_context", request.getContextPath());
        request.setAttribute("users", users);
        request.getRequestDispatcher("/show.ftlh").forward(request, response);
    }
}

