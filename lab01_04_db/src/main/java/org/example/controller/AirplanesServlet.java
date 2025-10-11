package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Airplane;
import org.example.repository.AirplaneRepository;
import org.example.service.AirplaneService;

import java.io.IOException;
import java.util.List;

@WebServlet("/airplanes")
public class AirplanesServlet extends HttpServlet {

    final static Logger logger = LogManager.getLogger(AirplanesServlet.class);

    private AirplaneService airplaneService = new AirplaneService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        airplaneService.fillAttributes(request);
        request.getRequestDispatcher("/airplanes.ftlh")
                .forward(request, response);
    }

}