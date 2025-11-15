package ru.freelib.controller.admin;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.freelib.model.Genre;
import ru.freelib.model.User;
import ru.freelib.service.GenreService;
import ru.freelib.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/admin/edit-genre")
public class GenreEditServlet extends HttpServlet {
    private UserService userService;
    private GenreService genreService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
        genreService = (GenreService) getServletContext().getAttribute("genreService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        Genre genre = genreService.getById(Long.parseLong(request.getParameter("id")));
        request.setAttribute("genre", genre);
        request.getRequestDispatcher("/admin/genre-edit.ftlh").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        Long id = Long.parseLong(request.getParameter("id"));
        if (Objects.equals(request.getParameter("delete"), "true")) {
            genreService.delete(id);
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        genreService.update(new Genre(id, name, description));
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}

