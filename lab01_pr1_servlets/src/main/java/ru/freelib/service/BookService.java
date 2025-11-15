package ru.freelib.service;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;
import ru.freelib.model.Book;
import ru.freelib.model.Genre;
import ru.freelib.model.User;
import ru.freelib.repository.BookDao;
import ru.freelib.repository.GenreDao;
import ru.freelib.repository.UserDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private final BookDao bookDao;
    private final UserDao userDao;
    private final GenreDao genreDao;
    private final ServletContext servletContext;

    public BookService(BookDao bookDao, UserDao userDao, GenreDao genreDao, ServletContext servletContext) {
        this.bookDao = bookDao;
        this.userDao = userDao;
        this.genreDao = genreDao;
        this.servletContext = servletContext;
    }

    public List<Book> findAll() {
        return bookDao.findAll();
    }

    public Book getById(Long id) {
        return bookDao.findById(id);
    }

    public void addBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Название книги не может быть пустым");
        }
        bookDao.save(book);
    }

    public void deleteBook(Long id) {
        bookDao.delete(id);
    }

    public List<Book> findByAuthor(Long id) {
        return bookDao.findByAuthorId(id);
    }

    public boolean uploadBook(String title,
                              String description,
                              Long userId,
                              Long genreId,
                              Part filePart) throws IOException {
        User user = userDao.findById(userId);
        Genre genre = genreDao.findById(genreId);
        if (user == null || genre == null) {
            throw new IllegalArgumentException("Некорректный пользователь или жанр");
        }
        String uploadPath = servletContext.getRealPath("/uploads");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String original = Paths.get(filePart.getSubmittedFileName())
                .getFileName()
                .toString();
        int dotIndex = original.lastIndexOf('.');
        String baseName = original.substring(0, dotIndex);
        String extension = original.substring(dotIndex + 1);
        String result = baseName + "_" + System.currentTimeMillis() + "." + extension;
        File savedFile = new File(uploadDir, result);
        filePart.write(savedFile.getAbsolutePath());
        Book book = new Book(title, description, result, user, genre);

        return bookDao.save(book);
    }

    public List<Book> getByGenreId(Long genreId) {
        return bookDao.findByGenreId(genreId);
    }

    public boolean isFavorite(Long userId, Long bookId) {
        return bookDao.isFavorite(userId, bookId);
    }

    public boolean delete(Long id) {
        Book book = bookDao.findById(id);
        if (book == null) return false;

        String uploadPath = servletContext.getRealPath("/uploads");
        File file = new File(uploadPath, book.getFilePath());

        if (file.exists()) {
            file.delete();
        }

        return bookDao.delete(id);
    }

    public boolean updateBook(Long id,
                              String title,
                              String description,
                              Long genreId,
                              Part filePart) throws IOException {

        Book book = bookDao.findById(id);
        Genre genre = genreDao.findById(genreId);

        if (book == null || genre == null) {
            throw new IllegalArgumentException("Книга или жанр не найдены");
        }

        String uploadPath = servletContext.getRealPath("/uploads");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String newFileName = book.getFilePath();

        if (filePart != null && filePart.getSize() > 0) {

            File oldFile = new File(uploadDir, book.getFilePath());
            if (oldFile.exists()) {
                oldFile.delete();
            }

            String original = Paths.get(filePart.getSubmittedFileName())
                    .getFileName().toString();

            int dot = original.lastIndexOf('.');
            String base = original.substring(0, dot);
            String ext = original.substring(dot + 1);

            newFileName = base + "_" + System.currentTimeMillis() + "." + ext;

            File newFile = new File(uploadDir, newFileName);
            filePart.write(newFile.getAbsolutePath());
        }

        book.setTitle(title);
        book.setDescription(description);
        book.setGenre(genre);
        book.setFilePath(newFileName);

        return bookDao.update(book);
    }
}
