package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.util.List;

public interface UserService {
    boolean updateUserProfile(User user) throws ServiceException;

    List<User> findAllUsers() throws ServiceException;
}
