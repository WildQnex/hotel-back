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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class UserServiceImplTest {

    private UserDao userDao;
    private User user;
    private UserService userService;
    private List<User> userList;
    private String currentPassword;
    private String newPassword;

    @BeforeClass
    public void setUp() throws Exception {
        userService = new UserServiceImpl();
        user = new User(1, "Vadim", "Alekseevich", "Martyniuk", new BigDecimal(0),
                "mail@gmail.com", "+375251712452", "$2a$10$dli9pv2bKHf9.OfGatlFrOFJaWRYR14C94VBX1jL33ckdbIiTEg9u", Role.ADMIN, true);
        userDao = mock(UserDao.class);
        UserServiceImpl.userDao = userDao;
        userList = new ArrayList<>();
        userList.add(user);
        userList.add(user);
        currentPassword = "useruser";
        newPassword = "user";

    }


    @Test
    public void updateUserProfileTest() throws ServiceException, DaoException {
        when(userDao.updateUserData(user)).thenReturn(true);
        assertTrue(userService.updateUserProfile(user));
    }

    @Test
    public void findAllUsersTest() throws ServiceException, DaoException {
        when(userDao.findAllUsers()).thenReturn(userList);
        assertEquals(userService.findAllUsers(), userList);
    }

    @Test
    public void findUserByMailTest() throws ServiceException, DaoException {
        when(userDao.findUserByMail(user.getEmail())).thenReturn(Optional.of(user));
        assertEquals(userService.findUserByMail(user.getEmail()), Optional.of(user));
    }

    @Test
    public void changeUserPasswordTest() throws ServiceException, DaoException {
        when(userDao.findUserByMail(user.getEmail())).thenReturn(Optional.of(user));
        when(userDao.updateUserPassword(user.getId(), newPassword)).thenReturn(true);
        assertTrue(userService.changeUserPassword(user.getEmail(), currentPassword, newPassword));
    }

}