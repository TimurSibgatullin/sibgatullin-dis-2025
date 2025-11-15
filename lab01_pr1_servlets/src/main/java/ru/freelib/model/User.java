package ru.freelib.model;

public class User {
    private Long id;
    private String login;
    private String passwordHash;
    private String role;
    private String nickname;
    private String description;

    public User() {
    }

    public User(Long id, String login, String passwordHash, String role, String nickname, String description) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
        this.nickname = nickname;
        this.description = description;
    }

    public User(String login, String passwordHash, String role, String nickname, String description) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
        this.nickname = nickname;
        this.description = description;
    }

    public User(Long id, String nickname, String description) {
        this.description = description;
        this.nickname = nickname;
        this.id = id;
    }

    public User(String login, String passwordHash, String role, String nickname) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
        this.nickname = nickname;
    }

    public User(Long id, String login, String passwordHash, String role, String nickname) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
