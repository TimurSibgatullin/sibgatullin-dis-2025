package ru.freelib.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import ru.freelib.model.Book;
import ru.freelib.model.Genre;
import ru.freelib.model.User;
import ru.freelib.service.BookService;
import ru.freelib.service.CommentService;
import ru.freelib.service.GenreService;

import java.io.IOException;
import java.util.List;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
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
        List<Genre> genres = genreService.findAll();
        request.setAttribute("genres", genres);
        request.getRequestDispatcher("/upload.ftlh").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        User user = (User) request.getSession().getAttribute("user");

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        long genreId = Long.parseLong(request.getParameter("genre"));

        Part filePart = request.getPart("file");
        bookService.uploadBook(title, description, user.getId(), genreId, filePart);

        response.sendRedirect(request.getContextPath() + "/profile");
    }
}