package ru.freelib.controller.favorite;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.freelib.model.User;
import ru.freelib.service.FavoriteService;

import java.io.IOException;

@WebServlet("/favorite/add")
public class AddFavoriteServlet extends HttpServlet {

    private FavoriteService favorites;

    @Override
    public void init() {
        favorites = (FavoriteService) getServletContext().getAttribute("favoriteService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Long userId = ((User) req.getSession().getAttribute("user")).getId();
        Long bookId = Long.parseLong(req.getParameter("id"));

        favorites.add(userId, bookId);

        resp.setContentType("text/plain; charset=UTF-8");
        resp.getWriter().write("OK");
    }
}