package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.util.List;

public interface UserDao {

    List<User> findAllUsers() throws DaoException;

    User findUserByMail(String mail) throws DaoException;

    boolean addUser(User user) throws DaoException;

    boolean updateUser(User user) throws DaoException;

}
