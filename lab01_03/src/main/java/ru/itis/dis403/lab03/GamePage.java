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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

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
        request.setAttribute("uid", uid);

        request.getRequestDispatcher("/game.ftlh").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uid = request.getParameter("uid");
        int row = Integer.parseInt(request.getParameter("row"));
        int col = Integer.parseInt(request.getParameter("column"));

        GameState game = gamers.get(uid);
        if (game == null) {
            response.sendRedirect("/lab03/game");
            return;
        }

        if (!game.isGameOver() && game.isEmpty(row, col)) {
            game.setCell(row, col, "крестик.png");
            if (game.checkWin("крестик.png")) {
                game.setGameOver(true);
                game.setMessage("Вы выиграли!");
            } else if (!game.hasEmpty()) {
                game.setGameOver(true);
                game.setMessage("Ничья!");
            } else {
                game.aiMove();
                if (game.checkWin("нолик.png")) {
                    game.setGameOver(true);
                    game.setMessage("Вы проиграли!");
                } else if (!game.hasEmpty()) {
                    game.setGameOver(true);
                    game.setMessage("Ничья!");
                }
            }
        }

        request.setAttribute("table", game.getTable());
        request.setAttribute("uid", uid);
        request.setAttribute("message", game.getMessage());
        request.setAttribute("gameOver", game.isGameOver());

        request.getRequestDispatcher("/game.ftlh").forward(request, response);
    }
}