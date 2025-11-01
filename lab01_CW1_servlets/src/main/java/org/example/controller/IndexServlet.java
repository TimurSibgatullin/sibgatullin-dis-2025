package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet("/")
public class IndexServlet extends HttpServlet {
    final static Logger logger = LogManager.getLogger(IndexServlet.class);
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("current_context", request.getContextPath());
        request.getRequestDispatcher("/index.ftlh")
                .forward(request, response);
    }
}
