package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.ApartmentClassService;
import by.martyniuk.hotelbooking.service.ApartmentService;
import by.martyniuk.hotelbooking.service.AuthorizationService;
import by.martyniuk.hotelbooking.service.ReservationService;
import by.martyniuk.hotelbooking.service.UserService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * The Class ActionCommandTest.
 */
public class ActionCommandTest {

    /**
     * The apartment service.
     */
    private ApartmentService apartmentService;

    /**
     * The apartment class service.
     */
    private ApartmentClassService apartmentClassService;

    /**
     * The authorization service.
     */
    private AuthorizationService authorizationService;

    /**
     * The reservation service.
     */
    private ReservationService reservationService;

    /**
     * The user service.
     */
    private UserService userService;

    /**
     * The request.
     */
    private MockHttpServletRequest request;

    /**
     * The user.
     */
    private User user;

    /**
     * The reservations.
     */
    private List<Reservation> reservations;

    /**
     * The apartment class.
     */
    private ApartmentClass apartmentClass;

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {
        apartmentService = mock(ApartmentService.class);
        apartmentClassService = mock(ApartmentClassService.class);
        authorizationService = mock(AuthorizationService.class);
        reservationService = mock(ReservationService.class);
        userService = mock(UserService.class);
        request = new MockHttpServletRequest();
        apartmentClass = new ApartmentClass();
        reservations = new ArrayList<>();
        reservations.add(new Reservation());
        reservations.add(new Reservation());
        user = new User(1, "Vadim", "Alekseevich", "Martyniuk", new BigDecimal(0),
                "mail@gmail.com", "+375251712452", "password", Role.ADMIN, true);
    }


    /**
     * Login test.
     *
     * @throws ServiceException the service exception
     * @throws CommandException the command exception
     */
    @Test
    public void loginTest() throws ServiceException, CommandException {
        request.setParameter("email", user.getEmail());
        request.setParameter("password", user.getPassword());
        CommandType.authorizationService = authorizationService;
        when(authorizationService.login(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
        CommandType.LOGIN.receiveCommand().execute(request);
        assertEquals(request.getSession().getAttribute("user"), user);
    }

    /**
     * Login error test.
     *
     * @throws ServiceException the service exception
     * @throws CommandException the command exception
     */
    @Test
    public void loginErrorTest() throws ServiceException, CommandException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("email", "user@gmail.com");
        request.setParameter("password", "password");
        CommandType.authorizationService = authorizationService;
        when(authorizationService.login("user@gmail.com", "password")).thenReturn(Optional.empty());
        CommandType.LOGIN.receiveCommand().execute(request);
        assertNotNull(request.getSession().getAttribute("loginError"));
    }

    /**
     * Logout test.
     *
     * @throws CommandException the command exception
     */
    @Test
    public void logoutTest() throws CommandException {
        request.getSession().setAttribute("user", user);
        CommandType.LOGOUT.receiveCommand().execute(request);
        assertNull(request.getSession().getAttribute("user"));
    }

    /**
     * Sets the locale test.
     *
     * @throws CommandException the command exception
     */
    @Test
    public void setLocaleTest() throws CommandException {
        String oldLocale = "ru_RU";
        String newLocale = "en_US";
        request.getSession().setAttribute("locale", oldLocale);
        request.setParameter("value", newLocale);
        CommandType.SET_LOCALE.receiveCommand().execute(request);
        assertEquals(request.getSession().getAttribute("locale"), newLocale);
    }

    /**
     * Forward test.
     *
     * @throws CommandException the command exception
     */
    @Test
    public void forwardTest() throws CommandException {
        request.setParameter("page", PagePath.MAIN.toString());
        CommandType.FORWARD.receiveCommand().execute(request);
        assertEquals(CommandType.FORWARD.receiveCommand().execute(request), PagePath.MAIN.getPage());
    }

    /**
     * Forward error test.
     *
     * @throws CommandException the command exception
     */
    @Test(expectedExceptions = CommandException.class)
    public void forwardErrorTest() throws CommandException {
        request.setParameter("page", "");
        CommandType.FORWARD.receiveCommand().execute(request);
    }

    /**
     * Register test.
     *
     * @throws CommandException the command exception
     * @throws ServiceException the service exception
     */
    @Test
    public void registerTest() throws CommandException, ServiceException {
        request.setParameter("firstName", user.getFirstName());
        request.setParameter("lastName", user.getLastName());
        request.setParameter("middleName", user.getMiddleName());
        request.setParameter("email", user.getEmail());
        request.setParameter("phoneNumber", user.getPhoneNumber());
        request.setParameter("password", user.getPassword());
        request.setParameter("repeatPassword", user.getPassword());
        CommandType.authorizationService = authorizationService;
        when(authorizationService.register(anyObject())).thenReturn(true);
        CommandType.REGISTER.receiveCommand().execute(request);
        assertNull(request.getSession().getAttribute("registerError"));
    }

    /**
     * Register validation test.
     *
     * @throws CommandException the command exception
     * @throws ServiceException the service exception
     */
    @Test
    public void registerValidationTest() throws CommandException, ServiceException {
        request.setParameter("firstName", user.getFirstName());
        request.setParameter("lastName", user.getLastName());
        request.setParameter("middleName", user.getMiddleName());
        request.setParameter("password", user.getPassword());
        request.setParameter("repeatPassword", user.getPassword());
        CommandType.authorizationService = authorizationService;
        when(authorizationService.register(anyObject())).thenReturn(true);
        CommandType.REGISTER.receiveCommand().execute(request);
        assertNotNull(request.getSession().getAttribute("registerError"));
    }

    /**
     * Show apartment class test.
     *
     * @throws CommandException the command exception
     * @throws ServiceException the service exception
     */
    @Test
    public void showApartmentClassTest() throws CommandException, ServiceException {
        request.setParameter("id", "2");
        CommandType.apartmentClassService = apartmentClassService;
        when(apartmentClassService.findApartmentClassById(2)).thenReturn(Optional.of(apartmentClass));
        CommandType.SHOW_APARTMENT_CLASS.receiveCommand().execute(request);
        assertEquals(request.getAttribute("apartmentClass"), apartmentClass);
    }

    /**
     * Show apartment class error test.
     *
     * @throws CommandException the command exception
     * @throws ServiceException the service exception
     */
    @Test
    public void showApartmentClassErrorTest() throws CommandException, ServiceException {
        CommandType.apartmentClassService = apartmentClassService;
        when(apartmentClassService.findApartmentClassById(anyInt())).thenReturn(Optional.of(apartmentClass));
        CommandType.SHOW_APARTMENT_CLASS.receiveCommand().execute(request);
        assertNotNull(request.getSession().getAttribute("apartmentClassesError"));
    }

    /**
     * Show persona reservations test.
     *
     * @throws CommandException the command exception
     * @throws ServiceException the service exception
     */
    @Test
    public void showPersonaReservationsTest() throws CommandException, ServiceException {
        request.getSession().setAttribute("user", user);
        CommandType.reservationService = reservationService;
        when(reservationService.readAllReservationByUserId(anyLong())).thenReturn(reservations);
        CommandType.SHOW_PERSONAL_RESERVATIONS.receiveCommand().execute(request);
        assertEquals(request.getAttribute("reservations"), reservations);
    }

    /**
     * Update profile test.
     *
     * @throws CommandException the command exception
     * @throws ServiceException the service exception
     */
    @Test
    public void updateProfileTest() throws CommandException, ServiceException {
        String name = "name";
        request.getSession().setAttribute("user", user);
        request.setParameter("firstName", name);
        request.setParameter("lastName", user.getLastName());
        request.setParameter("middleName", user.getMiddleName());
        request.setParameter("phoneNumber", user.getPhoneNumber());
        CommandType.userService = userService;
        when(userService.updateUserProfile(anyObject())).thenReturn(true);
        CommandType.UPDATE_PROFILE.receiveCommand().execute(request);
        assertEquals(((User) request.getSession().getAttribute("user")).getFirstName(), name);
    }

    /**
     * Update profile error test.
     *
     * @throws CommandException the command exception
     * @throws ServiceException the service exception
     */
    @Test
    public void updateProfileErrorTest() throws CommandException, ServiceException {
        request.getSession().setAttribute("user", user);
        request.setParameter("firstName", "");
        request.setParameter("lastName", user.getLastName());
        request.setParameter("middleName", user.getMiddleName());
        request.setParameter("phoneNumber", user.getPhoneNumber());
        CommandType.userService = userService;
        when(userService.updateUserProfile(anyObject())).thenReturn(true);
        CommandType.UPDATE_PROFILE.receiveCommand().execute(request);
        assertNotNull(request.getSession().getAttribute("updateProfileError"));
    }

    /**
     * Update user password test.
     *
     * @throws CommandException the command exception
     * @throws ServiceException the service exception
     */
    @Test
    public void updateUserPasswordTest() throws CommandException, ServiceException {
        String newPassword = "newPassword";
        request.getSession().setAttribute("user", user);
        request.setParameter("currentPassword", user.getPassword());
        request.setParameter("newPassword", newPassword);
        request.setParameter("repeatNewPassword", newPassword);
        CommandType.userService = userService;
        when(userService.changeUserPassword(user.getEmail(), user.getPassword(), newPassword)).thenReturn(true);
        user.setPassword(newPassword);
        when(userService.findUserByMail(user.getEmail())).thenReturn(Optional.of(user));
        CommandType.UPDATE_PROFILE.receiveCommand().execute(request);
        assertEquals(((User) request.getSession().getAttribute("user")).getPassword(), newPassword);
    }
}