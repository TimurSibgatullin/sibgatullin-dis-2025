package ru.freelib.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import ru.freelib.model.User;
import ru.freelib.repository.UserDao;

public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean register(String login, String password1, String password2, String role, String nickname) {
        if (!userDao.isLoginFree(login)) {
            return false;
        }

        String hash = BCrypt.hashpw(password1, BCrypt.gensalt());
        User user = new User(login, hash, role, nickname);
        return userDao.save(user);
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
}
