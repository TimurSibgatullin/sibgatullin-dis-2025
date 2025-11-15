package ru.freelib.Config;

import ru.freelib.repository.ConnectionManager;
import ru.freelib.repository.UserDao;
import ru.freelib.service.UserService;

public class CreateAdmin {
    private static UserService userService;
    private static ConnectionManager connectionManager;
    public static void main(String[] args) {
        connectionManager = new ConnectionManager();
        try {
            connectionManager.init();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        UserDao userDao = new UserDao(connectionManager);
        userService = new UserService(userDao);
        System.out.println(userService.register("admin", "HulTi55iYB}g+yXU", "HulTi55iYB}g+yXU", "admin", "admin"));
    }
}
