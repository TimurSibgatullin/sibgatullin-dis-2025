package ru.freelib.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.freelib.model.Book;
import ru.freelib.model.User;
import ru.freelib.service.BookService;
import ru.freelib.service.CommentService;
import ru.freelib.service.FavoriteService;

import java.io.IOException;
import java.net.URLEncoder;


@WebServlet("/book")
public class BookServlet extends HttpServlet {
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
        long bookId = Long.parseLong(request.getParameter("id"));
        Book book = bookService.getById(bookId);
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                boolean isFav = bookService.isFavorite(user.getId(), book.getId());
                book.setFavorite(isFav);
            }
        }
        request.setAttribute("book", bookService.getById(bookId));
        request.setAttribute("comments", commentService.getByBookId(bookId));
        request.setAttribute("book", book);
        request.getRequestDispatcher("/book.ftlh").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        long bookId = Long.parseLong(request.getParameter("bookId"));
        String text = URLEncoder.encode(request.getParameter("comment"));

        response.sendRedirect(request.getContextPath() + "/comment/add?bookId=" + bookId +"&text=" + text);
    }
}