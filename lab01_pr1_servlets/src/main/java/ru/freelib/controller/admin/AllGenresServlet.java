package ru.freelib.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.freelib.model.Genre;
import ru.freelib.model.User;
import ru.freelib.service.GenreService;
import ru.freelib.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/all-genres")
public class AllGenresServlet extends HttpServlet {
    private GenreService genreService;


    @Override
    public void init() {
        genreService = (GenreService) getServletContext().getAttribute("genreService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        List<Genre> genres = genreService.findAll();
        request.setAttribute("genres", genres);
        request.getRequestDispatcher("/admin/genres.ftlh").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
