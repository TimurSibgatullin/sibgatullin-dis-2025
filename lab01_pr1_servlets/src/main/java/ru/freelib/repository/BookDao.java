package ru.freelib.repository;

import ru.freelib.model.Book;
import ru.freelib.model.Genre;
import ru.freelib.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {
    private final ConnectionManager connectionManager;

    public BookDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public List<Book> findAll() {
        String sql = "SELECT b.id, b.title, b.description, b.file_path, b.views, " +
                "g.id AS genre_id, g.name AS genre_name, " +
                "u.id AS author_id, u.nickname AS author_name " +
                "FROM books b " +
                "LEFT JOIN genres g ON b.genre_id = g.id " +
                "LEFT JOIN users u ON b.author_id = u.id";
        List<Book> list = new ArrayList<>();

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getLong("id"));
                book.setTitle(rs.getString("title"));
                book.setDescription(rs.getString("description"));
                book.setFilePath(rs.getString("file_path"));
                book.setViews(rs.getLong("views"));

                Genre genre = new Genre();
                genre.setId(rs.getLong("genre_id"));
                genre.setName(rs.getString("genre_name"));
                book.setGenre(genre);

                User author = new User();
                author.setId(rs.getLong("author_id"));
                author.setNickname(rs.getString("author_name"));
                book.setAuthor(author);

                list.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Book findById(Long id) {
        String sql = "SELECT b.id, b.title, b.description, b.file_path, b.views, " +
                "g.id AS genre_id, g.name AS genre_name, " +
                "u.id AS author_id, u.nickname AS author_name " +
                "FROM books b " +
                "LEFT JOIN genres g ON b.genre_id = g.id " +
                "LEFT JOIN users u ON b.author_id = u.id " +
                "WHERE b.id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Book book = new Book();
                book.setId(rs.getLong("id"));
                book.setTitle(rs.getString("title"));
                book.setDescription(rs.getString("description"));
                book.setFilePath(rs.getString("file_path"));
                book.setViews(rs.getLong("views"));

                Genre genre = new Genre();
                genre.setId(rs.getLong("genre_id"));
                genre.setName(rs.getString("genre_name"));
                book.setGenre(genre);

                User author = new User();
                author.setId(rs.getLong("author_id"));
                author.setNickname(rs.getString("author_name"));
                book.setAuthor(author);
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean save(Book book) {
        String sql = "INSERT INTO books (title, description, file_path, author_id, genre_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getDescription());
            ps.setString(3, book.getFilePath());
            ps.setLong(4, book.getAuthor().getId());
            ps.setLong(5, book.getGenre().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Book> findByAuthorId(Long id) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.id, b.title, b.description, b.file_path, b.views, " +
                "g.id AS genre_id, g.name AS genre_name, " +
                "u.id AS author_id, u.nickname AS author_name " +
                "FROM books b " +
                "LEFT JOIN genres g ON b.genre_id = g.id " +
                "LEFT JOIN users u ON b.author_id = u.id " +
                "WHERE author_id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getLong("id"));
                book.setTitle(rs.getString("title"));
                book.setDescription(rs.getString("description"));
                book.setFilePath(rs.getString("file_path"));
                book.setViews(rs.getLong("views"));

                Genre genre = new Genre();
                genre.setId(rs.getLong("genre_id"));
                genre.setName(rs.getString("genre_name"));
                book.setGenre(genre);

                User author = new User();
                author.setId(rs.getLong("author_id"));
                author.setNickname(rs.getString("author_name"));
                book.setAuthor(author);

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> findByGenreId(Long genreId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.id, b.title, b.description, b.file_path, b.views, " +
                "g.id AS genre_id, g.name AS genre_name, " +
                "u.id AS author_id, u.nickname AS author_name " +
                "FROM books b " +
                "LEFT JOIN genres g ON b.genre_id = g.id " +
                "LEFT JOIN users u ON b.author_id = u.id " +
                "WHERE g.id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, genreId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getLong("id"));
                book.setTitle(rs.getString("title"));
                book.setDescription(rs.getString("description"));
                book.setFilePath(rs.getString("file_path"));
                book.setViews(rs.getLong("views"));

                Genre genre = new Genre();
                genre.setId(rs.getLong("genre_id"));
                genre.setName(rs.getString("genre_name"));
                book.setGenre(genre);

                User author = new User();
                author.setId(rs.getLong("author_id"));
                author.setNickname(rs.getString("author_name"));
                book.setAuthor(author);

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public boolean isFavorite(long userId, long bookId) {
        String sql = "SELECT 1 FROM user_book_favorites WHERE user_id = ? AND book_id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(Book book) {
        String sql = "UPDATE books SET title = ?, description = ?, file_path = ?, genre_id = ? WHERE id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getDescription());
            stmt.setString(3, book.getFilePath());
            stmt.setLong(4, book.getGenre().getId());
            stmt.setLong(5, book.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating book", e);
        }
    }
}
