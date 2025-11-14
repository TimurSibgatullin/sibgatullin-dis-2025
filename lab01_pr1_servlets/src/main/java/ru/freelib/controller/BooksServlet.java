package ru.freelib.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.freelib.model.Comment;
import ru.freelib.model.User;
import ru.freelib.service.BookService;
import ru.freelib.service.CommentService;
import ru.freelib.service.GenreService;

import java.io.IOException;

@WebServlet("/books")
public class BooksServlet extends HttpServlet {
    private BookService bookService;
    private CommentService commentService;
    private GenreService genreService;


    @Override
    public void init() {
        bookService = (BookService) getServletContext().getAttribute("bookService");
        genreService = (GenreService) getServletContext().getAttribute("genreService");
        commentService = (CommentService) getServletContext().getAttribute("commentService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        long genreId = Long.parseLong(request.getParameter("genreId"));
        request.setAttribute("books", bookService.getByGenreId(genreId));
        request.setAttribute("genre", genreService.getById(genreId));
        request.getRequestDispatcher("/books.ftlh").forward(request, response);
    }
}