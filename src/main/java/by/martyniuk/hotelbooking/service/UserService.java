package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * The Interface UserService.
 */
public interface UserService {

    /**
     * Update user profile.
     *
     * @param user the user
     * @return true, if successful
     * @throws ServiceException the service exception
     */
    boolean updateUserProfile(User user) throws ServiceException;

    /**
     * Find user by mail.
     *
     * @param mail the mail
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<User> findUserByMail(String mail) throws ServiceException;

    /**
     * Find user by id.
     *
     * @param userId the id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<User> findUserById(long userId) throws ServiceException;

    /**
     * Change user password.
     *
     * @param mail            the mail
     * @param currentPassword the current password
     * @param newPassword     the new password
     * @return true, if successful
     * @throws ServiceException the service exception
     */
    boolean changeUserPassword(String mail, String currentPassword, String newPassword) throws ServiceException;

    /**
     * Find all users.
     *
     * @return the list
     * @throws ServiceException the service exception
     */
    List<User> findAllUsers() throws ServiceException;

    /**
     * Deposit money.
     *
     * @param userId the user id
     * @param money  the money
     * @return true, if successful
     * @throws ServiceException the service exception
     */
    boolean depositMoney(long userId, BigDecimal money) throws ServiceException;
}
