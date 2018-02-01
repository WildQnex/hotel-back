package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> findAllUsers() throws DaoException;

    Optional<User> findUserByMail(String mail) throws DaoException;

    boolean addUser(User user) throws DaoException;

    boolean updateUserData(User user) throws DaoException;

    boolean updateUserPassword(long userId, String newPassword) throws DaoException;

}
