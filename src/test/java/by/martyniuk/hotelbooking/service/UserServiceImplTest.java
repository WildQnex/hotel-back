package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.UserDao;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.impl.UserServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * The Class UserServiceImplTest.
 */
public class UserServiceImplTest {

    /**
     * The user dao.
     */
    private UserDao userDao;

    /**
     * The user.
     */
    private User user;

    /**
     * The user service.
     */
    private UserServiceImpl userService;

    /**
     * The user list.
     */
    private List<User> userList;

    /**
     * The current password.
     */
    private String currentPassword;

    /**
     * The new password.
     */
    private String newPassword;

    /**
     * Sets the up.
     */
    @BeforeClass
    public void setUp() {
        userService = new UserServiceImpl();
        user = new User(1, "Vadim", "Alekseevich", "Martyniuk", new BigDecimal(0),
                "mail@gmail.com", "+375251712452", "$2a$10$dli9pv2bKHf9.OfGatlFrOFJaWRYR14C94VBX1jL33ckdbIiTEg9u", Role.ADMIN, true);
        userDao = mock(UserDao.class);
        userService.setUserDao(userDao);
        userList = new ArrayList<>();
        userList.add(user);
        userList.add(user);
        currentPassword = "useruser";
        newPassword = "user";

    }


    /**
     * Update user profile test.
     *
     * @throws ServiceException the service exception
     * @throws DaoException     the dao exception
     */
    @Test
    public void updateUserProfileTest() throws ServiceException, DaoException {
        when(userDao.updateUserData(user)).thenReturn(true);
        assertTrue(userService.updateUserProfile(user));
    }

    /**
     * Find all users test.
     *
     * @throws ServiceException the service exception
     * @throws DaoException     the dao exception
     */
    @Test
    public void findAllUsersTest() throws ServiceException, DaoException {
        when(userDao.findAllUsers()).thenReturn(userList);
        assertEquals(userService.findAllUsers(), userList);
    }

    /**
     * Find user by mail test.
     *
     * @throws ServiceException the service exception
     * @throws DaoException     the dao exception
     */
    @Test
    public void findUserByMailTest() throws ServiceException, DaoException {
        when(userDao.findUserByMail(user.getEmail())).thenReturn(Optional.of(user));
        assertEquals(userService.findUserByMail(user.getEmail()), Optional.of(user));
    }

    /**
     * Change user password test.
     *
     * @throws ServiceException the service exception
     * @throws DaoException     the dao exception
     */
    @Test
    public void changeUserPasswordTest() throws ServiceException, DaoException {
        when(userDao.findUserByMail(user.getEmail())).thenReturn(Optional.of(user));
        when(userDao.updateUserPassword(eq(user.getId()), anyString())).thenReturn(true);
        assertTrue(userService.changeUserPassword(user.getEmail(), currentPassword, newPassword));
    }

}