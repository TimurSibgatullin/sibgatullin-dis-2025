package ru.freelib.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.freelib.model.Book;
import ru.freelib.model.Genre;
import ru.freelib.model.User;
import ru.freelib.service.BookService;
import ru.freelib.service.CommentService;
import ru.freelib.service.FavoriteService;
import ru.freelib.service.GenreService;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private BookService bookService;
    private CommentService commentService;
    private GenreService genreService;
    private FavoriteService favoriteService;

    @Override
    public void init() {
        bookService = (BookService) getServletContext().getAttribute("bookService");
        genreService = (GenreService) getServletContext().getAttribute("genreService");
        commentService = (CommentService) getServletContext().getAttribute("commentService");
        favoriteService = (FavoriteService) getServletContext().getAttribute("favoriteService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/usercheck");
            return;
        }
        request.setAttribute("myFavoriteBooks", favoriteService.getFavorites(user.getId()));
        request.setAttribute("myBooks", bookService.findByAuthor(user.getId()));
        request.setAttribute("myComments", commentService.getByUser(user.getId()));
        request.getRequestDispatcher("/profile.ftlh").forward(request, response);
    }
}
