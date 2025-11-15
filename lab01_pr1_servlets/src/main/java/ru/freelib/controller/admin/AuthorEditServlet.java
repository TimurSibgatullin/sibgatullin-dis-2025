package ru.freelib.controller.admin;
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
import java.util.List;
import java.util.Objects;

@WebServlet("/admin/edit-author")
public class AuthorEditServlet extends HttpServlet {
    final static Logger logger = LogManager.getLogger(LoginServlet.class);
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
        User author = userService.findById(Long.parseLong(request.getParameter("id")));
        request.setAttribute("author", author);
        request.setAttribute("myBooks", bookService.findByAuthor(author.getId()));
        request.getRequestDispatcher("/admin/author-edit.ftlh").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        String description = request.getParameter("description");
        Long id = Long.parseLong(request.getParameter("id"));
        if (Objects.equals(request.getParameter("delete"), "true")) {
            userService.delete(id);
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        userService.update(new User(id, "", "", "author", nickname, description));
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}
