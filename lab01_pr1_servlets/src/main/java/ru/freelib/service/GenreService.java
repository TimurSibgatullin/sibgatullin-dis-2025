package ru.freelib.service;

import ru.freelib.model.Genre;
import ru.freelib.repository.GenreDao;

import java.util.List;

public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao dao) {
        this.genreDao = dao;
    }

    public List<Genre> getAllGenres() {
        return genreDao.findAll();
    }

    public Genre getById(Long id) {
        return genreDao.findById(id);
    }

    public List<Genre> findAll() {
        return genreDao.findAll();
    }
}
