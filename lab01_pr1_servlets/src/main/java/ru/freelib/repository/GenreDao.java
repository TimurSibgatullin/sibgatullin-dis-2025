package ru.freelib.repository;

import ru.freelib.model.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreDao {
    private final ConnectionManager connectionManager;

    public GenreDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public List<Genre> findAll() {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT id, name, description FROM genres ORDER BY name";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                genres.add(new Genre(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }

    public Genre findById(Long id) {
        String sql = "SELECT id, name, description FROM genres WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Genre(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
