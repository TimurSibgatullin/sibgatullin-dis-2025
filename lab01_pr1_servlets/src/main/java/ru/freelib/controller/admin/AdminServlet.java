package ru.freelib.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.freelib.model.User;
import ru.freelib.repository.UserDao;
import ru.freelib.service.BookService;
import ru.freelib.service.CommentService;
import ru.freelib.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
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
        request.getRequestDispatcher("/admin/panel.ftlh").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        String author = request.getParameter("author");
        User user = userService.findByNickname(author);
        if (user == null) {
            request.setAttribute("errormessage", "Автор не найден!");
            request.getRequestDispatcher("/admin/panel.ftlh").forward(request, response);
        } else {
            request.setAttribute("myBooks", bookService.findByAuthor(user.getId()));
            request.setAttribute("author", user);
            request.getRequestDispatcher("/admin/author-edit.ftlh").forward(request, response);
        }
    }
}
