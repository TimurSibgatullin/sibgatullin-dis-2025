package ru.freelib.service;

import ru.freelib.model.Book;
import ru.freelib.repository.FavoriteDao;

import java.util.List;

public class FavoriteService {
    private final FavoriteDao favoriteDao;

    public FavoriteService(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    public void add(Long userId, Long bookId) {
        favoriteDao.addFavorite(userId, bookId);
    }

    public void remove(Long userId, Long bookId) {
        favoriteDao.removeFavorite(userId, bookId);
    }

    public List<Book> getFavorites(Long userId) {
        return favoriteDao.getFavoritesByUser(userId);
    }
}
