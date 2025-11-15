package ru.freelib.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.freelib.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final ConnectionManager connectionManager;
    final static Logger logger = LogManager.getLogger(UserDao.class);

    public UserDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT id, login, role, nickname, description " +
                    "FROM users";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getLong("id"),
                            rs.getString("login"),
                            rs.getString("password_hash"),
                            rs.getString("role"),
                            rs.getString("nickname"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserByLogin(String login) {
        String sql = """
            SELECT u.id, u.login, u.password_hash, u.role, u.nickname, u.description
            FROM users u
            WHERE u.login = ?
        """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setLogin(resultSet.getString("login"));
                    user.setPasswordHash(resultSet.getString("password_hash"));
                    user.setRole(resultSet.getString("role"));
                    user.setNickname(resultSet.getString("nickname"));
                    user.setDescription(resultSet.getString("description"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long save(User user) {
        String insertUser = "INSERT INTO public.users (login, password_hash, role, nickname, description) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "RETURNING id";
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            Long user_id = null;
            try (PreparedStatement stUser = connection.prepareStatement(insertUser)) {
                stUser.setString(1, user.getLogin());
                stUser.setString(2, user.getPasswordHash());
                stUser.setString(3, user.getRole());
                stUser.setString(4, user.getNickname());
                stUser.setString(5, user.getDescription());
                try (ResultSet rs = stUser.executeQuery()) {
                    if (rs.next()) {
                        user_id = rs.getLong("id");
                    }
                }
            }
            connection.commit();
            return user_id;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isLoginFree(String login) {
        String sql = "SELECT 1 FROM users WHERE login = ?";
        try (Connection connection = connectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return !resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPasswordHash(String login) {
        String sql = "SELECT password_hash FROM users WHERE login = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("password_hash");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findById(Long userId) {
        String sql = """
            SELECT u.id, u.login, u.password_hash, u.role, u.nickname, u.description
            FROM users u
            WHERE u.id = ?
        """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setLogin(resultSet.getString("login"));
                    user.setPasswordHash(resultSet.getString("password_hash"));
                    user.setRole(resultSet.getString("role"));
                    user.setNickname(resultSet.getString("nickname"));
                    user.setDescription(resultSet.getString("description"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findByNickname(String author) {
        String sql = """
            SELECT u.id, u.login, u.password_hash, u.role, u.nickname, u.description
            FROM users u
            WHERE u.nickname = ?
        """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, author);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setLogin(resultSet.getString("login"));
                    user.setPasswordHash(resultSet.getString("password_hash"));
                    user.setRole(resultSet.getString("role"));
                    user.setNickname(resultSet.getString("nickname"));
                    user.setDescription(resultSet.getString("description"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findAllAuthors() {
        List<User> users = new ArrayList<>();
        String role = "author";
        String sql = "SELECT u.id, u.login, u.password_hash, u.role, u.nickname, u.description " +
                "FROM users u " +
                "WHERE u.role = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, role);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getLong("id"),
                            rs.getString("login"),
                            rs.getString("password_hash"),
                            rs.getString("role"),
                            rs.getString("nickname"),
                            rs.getString("description"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean delete(long id) {
        String insertUser = "delete from users " +
                "where id = ?";
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement stUser = connection.prepareStatement(insertUser)) {
                stUser.setLong(1, id);
                stUser.executeUpdate();
            }
            connection.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(User user) {
        String insertUser = "UPDATE users SET nickname = ?, description = ? " +
                "where id = ?";
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement stUser = connection.prepareStatement(insertUser)) {
                stUser.setString(1, user.getNickname());
                stUser.setString(2, user.getDescription());
                stUser.setLong(3, user.getId());
                stUser.executeUpdate();
            }
            connection.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
