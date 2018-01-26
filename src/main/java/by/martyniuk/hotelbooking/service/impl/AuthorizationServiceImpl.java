package by.martyniuk.hotelbooking.service.impl;

import by.martyniuk.hotelbooking.dao.UserDao;
import by.martyniuk.hotelbooking.dao.impl.UserDaoImpl;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.AuthorizationService;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.math.BigDecimal;
import java.util.Optional;

public class AuthorizationServiceImpl implements AuthorizationService {

    private static UserDao userDao = new UserDaoImpl();

    @Override
    public Optional<User> login(String mail, String password) throws ServiceException {
        try {
            Optional<User> user = userDao.findUserByMail(mail);
            if (user.isPresent() && BCrypt.checkpw(password, user.get().getPassword())) {
                return user;
            } else {
                return Optional.empty();
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean register(String firstName, String middleName, String lastName, String email, String phoneNumber, String password) throws ServiceException {
        try {
            Optional<User> user = userDao.findUserByMail(email);
            if (user.isPresent()) {
                return false;
            }
            return userDao.addUser(new User(0, firstName, middleName, lastName, new BigDecimal("0"), email,
                    phoneNumber, BCrypt.hashpw(password, BCrypt.gensalt()), Role.USER, true));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

}
