package example.repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import example.model.User;
import example.service.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    final static Logger logger = LogManager.getLogger(UserRepository.class);
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            Connection connection = DBConnection.getConnection();
            String sql = "SELECT id, login, password " +
                        "FROM public.registred_users";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setLogin(resultSet.getString("login"));
                    user.setPassword(resultSet.getString("password"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public boolean findByLogin(String login) {
        String sql = "SELECT id, login, password " +
                    "FROM public.registred_users " +
                    "WHERE login = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return false;
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public User getUserByLogin(String login) {
        String sql = """
            SELECT u.id, u.login, u.password, u.role,
                   ui.firstname, ui.lastname, ui.phone
            FROM registred_users u
            LEFT JOIN user_info ui ON u.id = ui.id
            WHERE u.login = ?
        """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId((int) resultSet.getLong("id"));
                    user.setLogin(resultSet.getString("login"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));
                    user.setFirstname(resultSet.getString("firstname"));
                    user.setLastname(resultSet.getString("lastname"));
                    user.setPhone(resultSet.getString("phone"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String checkPassword(String login) {
        String sql = "SELECT password " +
                "FROM public.registred_users " +
                "WHERE login = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getString("password");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean save(User user) {
        String insertUser = "INSERT INTO public.registred_users (login, password, role) " +
                            "VALUES (?, ?, ?) " +
                            "RETURNING id";
        String insertInfo = "INSERT INTO public.user_info (firstname, lastname, phone, id) " +
                            "VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            int user_id = 0;
            try (PreparedStatement stUser = connection.prepareStatement(insertUser)) {
                stUser.setString(1, user.getLogin());
                stUser.setString(2, user.getPassword());
                stUser.setString(3, user.getRole());

                try (ResultSet rs = stUser.executeQuery()) {
                    if (rs.next()) {
                        user_id = rs.getInt("id");
                    }
                }
            }

            try (PreparedStatement stInfo = connection.prepareStatement(insertInfo)) {
                stInfo.setString(1, user.getFirstname());
                stInfo.setString(2, user.getLastname());
                stInfo.setString(3, user.getPhone());
                stInfo.setInt(4, user_id);
                stInfo.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
