package ru.freelib.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.freelib.controller.auth.LoginServlet;
import ru.freelib.model.Book;
import ru.freelib.model.User;
import ru.freelib.service.BookService;
import ru.freelib.service.GenreService;
import ru.freelib.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


@WebServlet("/edit-my-book")
@MultipartConfig
public class MyBookEditServlet extends HttpServlet {
    final static Logger logger = LogManager.getLogger(ru.freelib.controller.admin.BookEditServlet.class);
    private BookService bookService;
    private GenreService genreService;

    @Override
    public void init() {
        bookService = (BookService) getServletContext().getAttribute("bookService");
        genreService = (GenreService) getServletContext().getAttribute("genreService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("currentContext", request.getContextPath());

        long id = Long.parseLong(request.getParameter("id"));
        Book book = bookService.getById(id);

        request.setAttribute("book", book);
        request.setAttribute("genres", genreService.findAll());

        request.getRequestDispatcher("/edit-my-book.ftlh").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long id = Long.parseLong(req.getParameter("id"));

        if ("true".equals(req.getParameter("delete"))) {
            bookService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        long genreId = Long.parseLong(req.getParameter("genre"));
        Part filePart = req.getPart("file");

        bookService.updateBook(id, title, description, genreId, filePart);

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}