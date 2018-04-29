package by.martyniuk.hotelbooking.service.impl;

import by.martyniuk.hotelbooking.dao.UserDao;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * The Class UserServiceImpl.
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * The user dao.
     */
    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

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
    public boolean depositMoney(long userId, BigDecimal money) throws ServiceException {
        try {
            userDao.depositMoney(userId, money);
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
    public Optional<User> findUserById(long userId) throws ServiceException {
        try {
            return userDao.findUserById(userId);
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
            return userDao.updateUserPassword(user.getId(), BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

}
