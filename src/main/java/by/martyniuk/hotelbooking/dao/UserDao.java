package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * The Interface UserDao.
 */
public interface UserDao {

    /**
     * Find all users.
     *
     * @return the list
     * @throws DaoException the dao exception
     */
    List<User> findAllUsers() throws DaoException;

    /**
     * Find user by mail.
     *
     * @param mail the mail
     * @return the optional
     * @throws DaoException the dao exception
     */
    Optional<User> findUserByMail(String mail) throws DaoException;

    /**
     * Find user by id.
     *
     * @param userId the id
     * @return the optional
     * @throws DaoException the dao exception
     */
    Optional<User> findUserById(long userId) throws DaoException;

    /**
     * Adds the user.
     *
     * @param user the user
     * @return true, if successful
     * @throws DaoException the dao exception
     */
    boolean addUser(User user) throws DaoException;

    /**
     * Update user data.
     *
     * @param user the user
     * @return true, if successful
     * @throws DaoException the dao exception
     */
    boolean updateUserData(User user) throws DaoException;

    boolean updateUser(User user) throws DaoException;

    /**
     * Update user password.
     *
     * @param userId      the user id
     * @param newPassword the new password
     * @return true, if successful
     * @throws DaoException the dao exception
     */
    boolean updateUserPassword(long userId, String newPassword) throws DaoException;

    /**
     * Deposit money.
     *
     * @param userId the user id
     * @param money  the money
     * @return true, if successful
     * @throws DaoException the dao exception
     */
    boolean depositMoney(long userId, BigDecimal money) throws DaoException;
}
