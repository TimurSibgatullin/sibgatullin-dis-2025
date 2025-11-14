package ru.freelib.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class ConnectionManager {

    private DataSource dataSource;


    public ConnectionManager() {
    }

    public void init() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/freelib");
        config.setUsername("postgres");
        config.setPassword("00000000");
        config.setConnectionTimeout(50000);
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        ((HikariDataSource) dataSource).close();
    }
}
