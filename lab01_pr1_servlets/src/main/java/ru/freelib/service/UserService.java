package ru.freelib.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import ru.freelib.model.User;
import ru.freelib.repository.UserDao;

import java.util.List;

public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean register(String login, String password1, String password2, String role, String nickname, String description) {
        if (!userDao.isLoginFree(login)) {
            return false;
        }

        String hash = BCrypt.hashpw(password1, BCrypt.gensalt());
        User user = new User(login, hash, role, nickname, description);
        return (userDao.save(user) > 0);
    }

    public User authenticate(String login, String password) {
        String hash = userDao.getPasswordHash(login);
        if (hash != null && BCrypt.checkpw(password, hash)) {
            return userDao.getUserByLogin(login);
        }
        return null;
    }

    public User findById(Long id) {
        return userDao.findById(id);
    }

    public boolean adminRegister(String role, String nickname, String description) {
        User user = new User("", "", role, nickname, description);
        return (userDao.save(user) > 0);
    }

    public User findByNickname(String author) {
        return userDao.findByNickname(author);
    }

    public List<User> findAllAuthors() {
        return userDao.findAllAuthors();
    }

    public boolean adminRegister(User user) {
        return (userDao.save(user) > 0);
    }

    public boolean delete(long id) {
        return userDao.delete(id);
    }

    public boolean update(User user) {
        return userDao.update(user);
    }
}
