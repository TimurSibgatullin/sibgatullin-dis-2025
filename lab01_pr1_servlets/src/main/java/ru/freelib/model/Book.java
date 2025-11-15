package ru.freelib.model;

public class Book {
    private Long id;
    private String title;
    private String description;
    private String filePath;
    private Long views;
    private User author;
    private Genre genre;
    private boolean favorite;



    public Book(Long id, String title, String description, String filePath, User author, Genre genre) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.author = author;
        this.genre = genre;
    }

    public Book() {}

    public Book(String title, String description, String filePath, User author, Genre genre) {
        this.description = description;
        this.title = title;
        this.filePath = filePath;
        this.author = author;
        this.genre = genre;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
