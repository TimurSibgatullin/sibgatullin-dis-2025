package org.example.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            try {
                Class.forName("org.postgresql.Driver");
                connection =
                        DriverManager.getConnection(
                                // адрес БД , имя пользователя, пароль
                                "jdbc:postgresql://localhost:5432/demo","postgres","00000000");
                return connection;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void releaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
