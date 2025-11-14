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
import ru.freelib.model.Genre;
import ru.freelib.service.BookService;
import ru.freelib.service.GenreService;

import java.io.IOException;
import java.util.List;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    final static Logger logger = LogManager.getLogger(LoginServlet.class);
    private GenreService genreService;
    private BookService bookService;

    @Override
    public void init() {
        genreService = (GenreService) getServletContext().getAttribute("genreService");
        bookService = (BookService) getServletContext().getAttribute("bookService");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        request.setAttribute("user", request.getSession().getAttribute("user"));
        List<Genre> genres = genreService.findAll();
        request.setAttribute("genres", genres);
        request.getRequestDispatcher("/index.ftlh")
                .forward(request, response);
    }
}
