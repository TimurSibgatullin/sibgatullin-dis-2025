package ru.freelib.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.freelib.controller.auth.LoginServlet;
import ru.freelib.model.User;
import ru.freelib.service.BookService;
import ru.freelib.service.UserService;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/profile-edit")
public class ProfileEditServlet extends HttpServlet {

    private UserService userService;
    private BookService bookService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
        bookService = (BookService) getServletContext().getAttribute("bookService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        User author = (User) request.getSession().getAttribute("user");
        request.setAttribute("author", author);
        request.setAttribute("myBooks", bookService.findByAuthor(author.getId()));
        request.getRequestDispatcher("/profile-edit.ftlh").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        String description = request.getParameter("description");
        Long id = ((User) request.getSession().getAttribute("user")).getId();
        if (Objects.equals(request.getParameter("delete"), "true")) {
            userService.delete(id);
            response.sendRedirect(request.getContextPath() + "/auth/logout");
            return;
        }
        userService.update(new User(id, nickname, description));
        response.sendRedirect(request.getContextPath() + "/auth/logout");
    }
}
