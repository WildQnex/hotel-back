package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.util.Optional;

public interface AuthorizationService {
    Optional<User> login(String mail, String password) throws ServiceException;

    boolean register(User user) throws ServiceException;
}
