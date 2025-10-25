package org.example.repository;

import org.example.model.FlightInfo;
import org.example.service.DBConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightRepository {

    public static List<FlightInfo> findFlights(String airportCode, String date, String type) {
        List<FlightInfo> result = new ArrayList<>();
        String sql = type.equals("arrival") ?
                """
                SELECT f.flight_id, f.scheduled_departure, f.scheduled_arrival, f.status,
                       a1.city ->> 'ru' AS city, a1.airport_name ->> 'ru' AS airport_name
                FROM bookings.flights f
                JOIN bookings.routes r ON f.route_no = r.route_no
                JOIN bookings.airports_data a1 ON r.departure_airport = a1.airport_code
                JOIN bookings.airports_data a2 ON r.arrival_airport = a2.airport_code
                WHERE a2.airport_code = ? AND DATE(f.scheduled_arrival) = ?
                ORDER BY f.scheduled_arrival
                """
                :
                """
                SELECT f.flight_id, f.scheduled_departure, f.scheduled_arrival, f.status,
                       a2.city ->> 'ru' AS city, a2.airport_name ->> 'ru' AS airport_name
                FROM bookings.flights f
                JOIN bookings.routes r ON f.route_no = r.route_no
                JOIN bookings.airports_data a1 ON r.departure_airport = a1.airport_code
                JOIN bookings.airports_data a2 ON r.arrival_airport = a2.airport_code
                WHERE a1.airport_code = ? AND DATE(f.scheduled_departure) = ?
                ORDER BY f.scheduled_departure
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, airportCode);
            ps.setDate(2, java.sql.Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FlightInfo info = new FlightInfo();
                    info.setFlightId(rs.getInt("flight_id"));
                    info.setDeparture(rs.getTimestamp("scheduled_departure").toLocalDateTime());
                    info.setArrival(rs.getTimestamp("scheduled_arrival").toLocalDateTime());
                    info.setStatus(rs.getString("status"));
                    info.setCity(rs.getString("city"));
                    info.setAirport(rs.getString("airport_name"));
                    result.add(info);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}