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
        String fileName = String.join(List.of(original.split("\\.")).removeLast(), ".") + "_" + System.currentTimeMillis() +
                List.of(original.split("\\.")).getLast();
        File savedFile = new File(uploadDir, fileName);
        filePart.write(savedFile.getAbsolutePath());
        Book book = new Book(title, description, fileName, user, genre);

        return bookDao.save(book);
    }

    public List<Book> getByGenreId(Long genreId) {
        return bookDao.findByGenreId(genreId);
    }
}
