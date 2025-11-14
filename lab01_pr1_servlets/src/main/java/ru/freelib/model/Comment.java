package ru.freelib.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private Book book;
    private User user;
    private String text;
    private String created_at;

    public Comment(Long id, Book book, User user, String text, String created_at) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.text = text;
        this.created_at = created_at;
    }

    public Comment(Book book, User user, String text) {
        this.book = book;
        this.user = user;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
