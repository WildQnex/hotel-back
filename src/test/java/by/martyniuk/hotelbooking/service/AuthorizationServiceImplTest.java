package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.UserDao;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.impl.AuthorizationServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AuthorizationServiceImplTest {

    private UserDao userDao;
    private AuthorizationService authorizationService = new AuthorizationServiceImpl();
    private User user;
    private String correctPassword;
    private String incorrectPassword;


    @BeforeClass
    public void setUp() {
        userDao = mock(UserDao.class);
        user = new User(1, "Vadim", "Alekseevich", "Martyniuk", new BigDecimal(0),
                "mail@gmail.com", "+375251712452", "$2a$10$dli9pv2bKHf9.OfGatlFrOFJaWRYR14C94VBX1jL33ckdbIiTEg9u", Role.ADMIN, true);
        correctPassword = "useruser";
        incorrectPassword = "user";
    }


    @Test
    public void authorizationTest() throws DaoException, ServiceException {
        AuthorizationServiceImpl.userDao = userDao;
        when(userDao.findUserByMail(user.getEmail())).thenReturn(Optional.of(user));
        assertEquals(authorizationService.login(user.getEmail(), correctPassword), Optional.of(user));
    }

    @Test
    public void authorizationIncorrectPasswordTest() throws DaoException, ServiceException {
        AuthorizationServiceImpl.userDao = userDao;
        when(userDao.findUserByMail(user.getEmail())).thenReturn(Optional.of(user));
        assertEquals(authorizationService.login(user.getEmail(), incorrectPassword), Optional.empty());
    }

    @Test
    public void authorizationIncorrectMailTest() throws DaoException, ServiceException {
        AuthorizationServiceImpl.userDao = userDao;
        when(userDao.findUserByMail(user.getEmail())).thenReturn(Optional.empty());
        assertEquals(authorizationService.login(user.getEmail(), correctPassword), Optional.empty());
    }

    @Test
    public void registerTest() throws DaoException, ServiceException {
        AuthorizationServiceImpl.userDao = userDao;
        when(userDao.findUserByMail(user.getEmail())).thenReturn(Optional.empty());
        when(userDao.addUser(anyObject())).thenReturn(true);
        assertTrue(authorizationService.register(user));
    }
}