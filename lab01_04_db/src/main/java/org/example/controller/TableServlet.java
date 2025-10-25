package org.example.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.FlightInfo;
import org.example.repository.FlightRepository;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@WebServlet("/table")
public class TableServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String airport = req.getParameter("airport");
        String type = req.getParameter("type");
        String date = req.getParameter("date");

        if (date == null || date.isEmpty()) {
            date = LocalDate.now().toString();
        }

        List<FlightInfo> flights = FlightRepository.findFlights(airport, date, type);

        req.setAttribute("flights", flights);
        req.getRequestDispatcher("/table.ftlh").forward(req, resp);
    }
}