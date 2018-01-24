package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.UserDao;
import by.martyniuk.hotelbooking.dao.impl.UserDaoImpl;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.math.BigDecimal;

public class AuthorizationService {
    public static User login(String mail, String password) throws ServiceException {
        try {
            UserDao dao = new UserDaoImpl();
            User user = dao.findUserByMail(mail);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            } else {
                return null;
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public static boolean register(String firstName, String middleName, String lastName, String email, String phoneNumber, String password) throws ServiceException {
        try {
            UserDao dao = new UserDaoImpl();
            User user = dao.findUserByMail(email);
            if (user != null) {
                return false;
            }
            return dao.addUser(new User(0, firstName, middleName, lastName, new BigDecimal("0"), email, phoneNumber, password, Role.USER, true));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

}
