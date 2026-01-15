package ru.freelib.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.freelib.model.Book;
import ru.freelib.model.Genre;
import ru.freelib.model.User;
import ru.freelib.service.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private BookService bookService;
    private CommentService commentService;
    private GenreService genreService;
    private FavoriteService favoriteService;
    private UserService userService;

    @Override
    public void init() {
        bookService = (BookService) getServletContext().getAttribute("bookService");
        genreService = (GenreService) getServletContext().getAttribute("genreService");
        commentService = (CommentService) getServletContext().getAttribute("commentService");
        favoriteService = (FavoriteService) getServletContext().getAttribute("favoriteService");
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        Long userId = Long.parseLong(request.getParameter("id"));
        User user = (User) request.getSession().getAttribute("user");
        request.setAttribute("user", user);
        Long anotherUserId = Long.parseLong(request.getParameter("id"));
        User anotherUser = userService.findById(anotherUserId);
        request.setAttribute("anotherUser", anotherUser);
        request.setAttribute("myFavoriteBooks", favoriteService.getFavorites(anotherUser.getId()));
        request.setAttribute("myBooks", bookService.findByAuthor(anotherUser.getId()));
        request.setAttribute("myComments", commentService.getByUser(anotherUser.getId()));
        request.getRequestDispatcher("/public-profile.ftlh").forward(request, response);
    }
}
