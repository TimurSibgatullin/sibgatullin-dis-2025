package org.example.orm.reflection;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class EntityManagerFactory {
    private final DataSource dataSource;

    public EntityManagerFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static EntityManagerFactory create(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        HikariDataSource dataSource = new HikariDataSource(config);
        return new EntityManagerFactory(dataSource);
    }

    public EntityManagerImpl createEntityManager() {
        try {
            Connection connection = dataSource.getConnection();
            return new EntityManagerImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating EntityManager", e);
        }
    }

    public void close() {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }
}
