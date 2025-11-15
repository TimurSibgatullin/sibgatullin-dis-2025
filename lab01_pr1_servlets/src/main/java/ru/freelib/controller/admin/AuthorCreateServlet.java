package ru.freelib.controller.admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.freelib.model.Genre;
import ru.freelib.model.User;
import ru.freelib.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/create-author")
public class AuthorCreateServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("currentContext", request.getContextPath());
        request.getRequestDispatcher("/admin/author-create.ftlh").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        String description = request.getParameter("description");
        userService.adminRegister(new User("", "", "author", nickname, description));
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}
