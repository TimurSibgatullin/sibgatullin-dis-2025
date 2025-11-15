package ru.freelib.controller.admin;

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
import ru.freelib.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/upload")
@MultipartConfig
public class AdminUploadServlet extends HttpServlet {
    private UserService userService;
    private BookService bookService;
    private CommentService commentService;
    private GenreService genreService;
    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
        bookService = (BookService) getServletContext().getAttribute("bookService");
        genreService = (GenreService) getServletContext().getAttribute("genreService");
        commentService = (CommentService) getServletContext().getAttribute("commentService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        User user = (User) userService.findById(Long.parseLong(request.getParameter("id")));
        request.setAttribute("user", user);
        List<Genre> genres = genreService.findAll();
        request.setAttribute("genres", genres);
        request.getRequestDispatcher("/admin/book-upload.ftlh").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        User user = (User) userService.findById(Long.parseLong(request.getParameter("id")));

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        long genreId = Long.parseLong(request.getParameter("genre"));

        Part filePart = request.getPart("file");
        bookService.uploadBook(title, description, user.getId(), genreId, filePart);

        response.sendRedirect(request.getContextPath() + "/admin");
    }
}
