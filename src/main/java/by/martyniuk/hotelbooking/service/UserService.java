package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.UserDao;
import by.martyniuk.hotelbooking.dao.impl.UserDaoImpl;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.util.List;

public class UserService {

    public static boolean updateUserProfile(User user) throws ServiceException {
        try {
            UserDao dao = new UserDaoImpl();
            dao.updateUser(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return true;
    }


    public static List<User> findAllUsers() throws ServiceException {
        try {
            UserDao dao = new UserDaoImpl();
            return dao.findAllUsers();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
