package by.martyniuk.hotelbooking.service.impl;

import by.martyniuk.hotelbooking.dao.UserDao;
import by.martyniuk.hotelbooking.dao.impl.UserDaoImpl;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.UserService;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    public static UserDao userDao = new UserDaoImpl();

    @Override
    public boolean updateUserProfile(User user) throws ServiceException {
        try {
            userDao.updateUserData(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return true;
    }

    @Override
    public List<User> findAllUsers() throws ServiceException {
        try {
            return userDao.findAllUsers();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<User> findUserByMail(String mail) throws ServiceException {
        try {
            return userDao.findUserByMail(mail);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean changeUserPassword(String mail, String currentPassword, String newPassword) throws ServiceException {
        try {
            Optional<User> optionalUser = userDao.findUserByMail(mail);
            if (!(optionalUser.isPresent() && BCrypt.checkpw(currentPassword, optionalUser.get().getPassword()))) {
                return false;
            }
            User user = optionalUser.get();
            user.setPassword(newPassword);
            return userDao.updateUserPassword(user.getId(), newPassword);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

}
