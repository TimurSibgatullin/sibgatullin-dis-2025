package ru.freelib.service;

import ru.freelib.model.Comment;
import ru.freelib.repository.CommentDao;

import java.util.List;

public class CommentService {
    private final CommentDao commentDao;

    public CommentService(CommentDao dao) {
        this.commentDao = dao;
    }

    public boolean addComment(Comment c) {
        return commentDao.save(c);
    }


    public List<Comment> getByUser(Long id) {
        return commentDao.findByUserId(id);
    }

    public List<Comment> getByBookId(Long bookId) {
        return commentDao.findByBookId(bookId);
    }
}
