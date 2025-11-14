package ru.freelib.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.freelib.repository.*;
import ru.freelib.service.*;

@WebListener
public class AppContextListener implements ServletContextListener {
    private ConnectionManager connectionManager;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        connectionManager = new ConnectionManager();
        try {
            connectionManager.init();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        UserDao userDao = new UserDao(connectionManager);
        GenreDao genreDao = new GenreDao(connectionManager);
        BookDao bookDao = new BookDao(connectionManager);
        CommentDao commentDao = new CommentDao(connectionManager, bookDao, userDao);
        FavoriteDao favoriteDao = new FavoriteDao(connectionManager, userDao, genreDao);

        ServletContext context = sce.getServletContext();

        UserService userService = new UserService(userDao);
        GenreService genreService = new GenreService(genreDao);
        BookService bookService = new BookService(bookDao, userDao, genreDao, context);
        CommentService commentService = new CommentService(commentDao);
        FavoriteService favoriteService = new FavoriteService(favoriteDao);

        context.setAttribute("favoriteService", favoriteService);
        context.setAttribute("userService", userService);
        context.setAttribute("bookService", bookService);
        context.setAttribute("genreService", genreService);
        context.setAttribute("commentService", commentService);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        connectionManager.destroy();
    }
}

