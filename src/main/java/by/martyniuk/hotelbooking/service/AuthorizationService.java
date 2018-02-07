package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.util.Optional;

/**
 * The Interface AuthorizationService.
 */
public interface AuthorizationService {

    /**
     * Login.
     *
     * @param mail     the mail
     * @param password the password
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<User> login(String mail, String password) throws ServiceException;

    /**
     * Register.
     *
     * @param user the user
     * @return true, if successful
     * @throws ServiceException the service exception
     */
    boolean register(User user) throws ServiceException;
}
