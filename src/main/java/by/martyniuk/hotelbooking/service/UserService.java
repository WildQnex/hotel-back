package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    boolean updateUserProfile(User user) throws ServiceException;

    Optional<User> findUserByMail(String mail) throws ServiceException;

    boolean changeUserPassword(long userId, String currentPassword, String newPassword) throws ServiceException;

    List<User> findAllUsers() throws ServiceException;
}
