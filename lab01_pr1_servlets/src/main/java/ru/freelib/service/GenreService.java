package ru.freelib.service;

import ru.freelib.model.Genre;
import ru.freelib.repository.GenreDao;

import java.util.List;

public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao dao) {
        this.genreDao = dao;
    }

    public Genre getById(Long id) {
        return genreDao.findById(id);
    }

    public List<Genre> findAll() {
        return genreDao.findAll();
    }

    public boolean save(Genre genre) {
        return genreDao.save(genre);
    }

    public boolean update(Genre genre) {
        return genreDao.update(genre);
    }

    public boolean delete(Long id) {
        return genreDao.delete(id);
    }
}
