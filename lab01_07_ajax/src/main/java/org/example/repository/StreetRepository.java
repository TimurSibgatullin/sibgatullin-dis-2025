package org.example.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.City;
import org.example.model.Street;
import org.example.service.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StreetRepository {

    final static Logger logger = LogManager.getLogger(StreetRepository.class);

    public List<Street> getAll() throws Exception {
        List<Street> result = new ArrayList<>();

        Connection connection = DBConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "select id, name, city_id from street ");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            result.add(new Street(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getLong("city_id")));
        }
        resultSet.close();
        statement.close();
        connection.close();

        return result;
    }

    public List<Street> findByCityId(Long cityId) throws Exception {
        List<Street> result = new ArrayList<>();

        Connection connection = DBConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "select id, name, city_id from street where city_id = ? ");
        statement.setLong(1, cityId);

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            result.add(new Street(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getLong("city_id")));
        }
        resultSet.close();
        statement.close();
        connection.close();

        return result;
    }

}
