package ru.freelib.model;

import java.time.LocalDateTime;

public class Favorite {
    private Long userId;
    private Long bookId;
    private LocalDateTime addedAt;

    public Favorite(Long userId, Long bookId, LocalDateTime addedAt) {
        this.userId = userId;
        this.bookId = bookId;
        this.addedAt = addedAt;
    }

    public Long getUserId() { return userId; }
    public Long getBookId() { return bookId; }
    public LocalDateTime getAddedAt() { return addedAt; }
}
