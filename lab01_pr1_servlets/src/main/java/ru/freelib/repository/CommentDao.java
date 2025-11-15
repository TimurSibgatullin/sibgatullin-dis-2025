package ru.freelib.repository;

import ru.freelib.model.Comment;
import ru.freelib.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    private final ConnectionManager connectionManager;
    private final BookDao bookDao;
    private final UserDao userDao;

    public CommentDao(ConnectionManager connectionManager, BookDao bookDao, UserDao userDao) {
        this.connectionManager = connectionManager;
        this.bookDao = bookDao;
        this.userDao = userDao;
    }

    public boolean save(Comment comment) {
        String sql = "INSERT INTO comments (book_id, user_id, text) VALUES (?, ?, ?)";
        try(Connection connection = connectionManager.getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, comment.getBook().getId());
            preparedStatement.setLong(2, comment.getUser().getId());
            preparedStatement.setString(3, comment.getText());
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return false;
    }

    public List<Comment> findByBookId(Long bookId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT id, book_id, user_id, text, created_at FROM comments WHERE book_id = ? ORDER BY created_at DESC";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("created_at");
                    String localDateTime = ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
                    Comment c = new Comment(
                            rs.getLong("id"),
                            bookDao.findById(rs.getLong("book_id")),
                            userDao.findById(rs.getLong("user_id")),
                            rs.getString("text"),
                            localDateTime
                    );
                    comments.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public List<Comment> findByUserId(Long userId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT id, book_id, user_id, text, created_at FROM comments WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("created_at");
                    String localDateTime = ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
                    Comment c = new Comment(
                            rs.getLong("id"),
                            bookDao.findById(rs.getLong("book_id")),
                            userDao.findById(rs.getLong("user_id")),
                            rs.getString("text"),
                            localDateTime
                    );
                    comments.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
}

