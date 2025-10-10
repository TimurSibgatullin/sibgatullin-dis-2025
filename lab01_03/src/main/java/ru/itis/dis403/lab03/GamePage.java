package ru.itis.dis403.lab03;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/game")
public class GamePage extends HttpServlet {
    final static Logger logger = LogManager.getLogger(GamePage.class);
    private final Map<String, GameState> gamers = new HashMap<>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug(request.getServletPath());
        String uid = UUID.randomUUID().toString();
        GameState gameState = new GameState();
        gamers.put(uid, gameState);
        request.setAttribute("table", gameState.getTable());
        request.getRequestDispatcher("game.ftlh")
                .forward(request, response);
    }
}
