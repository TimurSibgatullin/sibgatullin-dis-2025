package ru.freelib.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.freelib.controller.auth.LoginServlet;
import ru.freelib.model.Book;
import ru.freelib.model.Comment;
import ru.freelib.model.User;
import ru.freelib.service.BookService;
import ru.freelib.service.CommentService;

import java.io.IOException;

@WebServlet("/comment/add")
public class CommentServlet extends HttpServlet {
    final static Logger logger = LogManager.getLogger(CommentServlet.class);
    private BookService bookService;
    private CommentService commentService;

    @Override
    public void init() {
        bookService = (BookService) getServletContext().getAttribute("bookService");
        commentService = (CommentService) getServletContext().getAttribute("commentService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());

        User user = ((User) request.getSession().getAttribute("user"));
        Book book =  bookService.getById(Long.parseLong(request.getParameter("bookId")));
        String text = request.getParameter("text");
        commentService.addComment(new Comment(book, user, text));
        response.sendRedirect(request.getContextPath() + "/book?id=" + book.getId());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}