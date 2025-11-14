package ru.freelib.repository;

import ru.freelib.model.Book;
import ru.freelib.model.Genre;
import ru.freelib.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDao {
    private final ConnectionManager connectionManager;
    private final UserDao userDao;
    private final GenreDao genreDao;

    public FavoriteDao(ConnectionManager connectionManager, UserDao userDao, GenreDao genreDao) {
        this.connectionManager = connectionManager;
        this.userDao = userDao;
        this.genreDao = genreDao;
    }

    public boolean addFavorite(Long userId, Long bookId) {
        String sql = "INSERT INTO user_book_favorites (user_id, book_id) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, bookId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding favorite", e);
        }
    }

    public boolean removeFavorite(Long userId, Long bookId) {
        String sql = "DELETE FROM user_book_favorites WHERE user_id = ? AND book_id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, bookId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error removing favorite", e);
        }
    }

    public List<Book> getFavoritesByUser(Long userId) {
        String sql = """
        SELECT b.id, b.title, b.description, b.file_path,
               u.id AS author_id, u.nickname,
               g.id AS genre_id, g.name AS genre_name, g.description AS genre_description
        FROM user_book_favorites f
        JOIN books b ON b.id = f.book_id
        JOIN users u ON u.id = b.author_id
        JOIN genres g ON g.id = b.genre_id
        WHERE f.user_id = ?
        ORDER BY f.added_at DESC
        """;

        List<Book> result = new ArrayList<>();

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User author = userDao.findById(rs.getLong("author_id"));

                    Genre genre = genreDao.findById(rs.getLong("genre_id"));

                    Book book = new Book(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("file_path"),
                            author,
                            genre
                    );
                    result.add(book);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting favorites", e);
        }

        return result;
    }

}
