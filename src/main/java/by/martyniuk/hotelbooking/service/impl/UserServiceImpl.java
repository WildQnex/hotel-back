package by.martyniuk.hotelbooking.service.impl;

import by.martyniuk.hotelbooking.dao.UserDao;
import by.martyniuk.hotelbooking.dao.impl.UserDaoImpl;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    @Override
    public boolean updateUserProfile(User user) throws ServiceException {
        try {
            UserDao dao = new UserDaoImpl();
            dao.updateUserData(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return true;
    }

    @Override
    public List<User> findAllUsers() throws ServiceException {
        try {
            UserDao dao = new UserDaoImpl();
            return dao.findAllUsers();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<User> findUserByMail(String mail) throws ServiceException {
        try {
            UserDao dao = new UserDaoImpl();
            return dao.findUserByMail(mail);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean changeUserPassword(long userId, String currentPassword, String newPassword) throws ServiceException{
        return true;
    }

}
