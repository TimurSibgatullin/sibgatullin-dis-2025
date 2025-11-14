package ru.freelib.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.freelib.model.Book;
import ru.freelib.model.Genre;
import ru.freelib.service.BookService;
import ru.freelib.service.GenreService;

import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private GenreService genreService;
    private BookService bookService;

    @Override
    public void init() {
        genreService = (GenreService) getServletContext().getAttribute("genreService");
        bookService = (BookService) getServletContext().getAttribute("bookService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("currentContext", request.getContextPath());
        List<Genre> genres = genreService.findAll();
        List<Book> books = bookService.findAll();
        request.setAttribute("genres", genres);
        request.setAttribute("books", books);
        request.getRequestDispatcher("/home.ftlh").forward(request, response);
    }
}