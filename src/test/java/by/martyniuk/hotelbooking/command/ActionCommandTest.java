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

public class ActionCommandTest {

    private ApartmentService apartmentService;
    private ApartmentClassService apartmentClassService;
    private AuthorizationService authorizationService;
    private ReservationService reservationService;
    private UserService userService;
    private MockHttpServletRequest request;
    private User user;
    private List<Reservation> reservations;
    private ApartmentClass apartmentClass;

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


    @Test
    public void loginTest() throws ServiceException, CommandException {
        request.setParameter("email", user.getEmail());
        request.setParameter("password", user.getPassword());
        CommandType.authorizationService = authorizationService;
        when(authorizationService.login(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
        CommandType.LOGIN.receiveCommand().execute(request);
        assertEquals(request.getSession().getAttribute("user"), user);
    }

    @Test
    public void loginErrorTest() throws ServiceException, CommandException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("email", "user@gmail.com");
        request.setParameter("password", "password");
        CommandType.authorizationService = authorizationService;
        when(authorizationService.login("user@gmail.com", "password")).thenReturn(Optional.empty());
        CommandType.LOGIN.receiveCommand().execute(request);
        assertNotNull(request.getSession().getAttribute("login_error"));
    }

    @Test
    public void logoutTest() throws CommandException {
        request.getSession().setAttribute("user", user);
        CommandType.LOGOUT.receiveCommand().execute(request);
        assertNull(request.getSession().getAttribute("user"));
    }

    @Test
    public void setLocaleTest() throws CommandException {
        String oldLocale = "ru_RU";
        String newLocale = "en_US";
        request.getSession().setAttribute("locale", oldLocale);
        request.setParameter("value", newLocale);
        CommandType.SET_LOCALE.receiveCommand().execute(request);
        assertEquals(request.getSession().getAttribute("locale"), newLocale);
    }

    @Test
    public void forwardTest() throws CommandException {
        request.setParameter("page", PagePath.MAIN.toString());
        CommandType.FORWARD.receiveCommand().execute(request);
        assertEquals(CommandType.FORWARD.receiveCommand().execute(request), PagePath.MAIN.getPage());
    }

    @Test(expectedExceptions = CommandException.class)
    public void forwardErrorTest() throws CommandException {
        request.setParameter("page", "");
        CommandType.FORWARD.receiveCommand().execute(request);
    }

    @Test
    public void registerTest() throws CommandException, ServiceException {
        request.setParameter("first_name", user.getFirstName());
        request.setParameter("last_name", user.getLastName());
        request.setParameter("middle_name", user.getMiddleName());
        request.setParameter("email", user.getEmail());
        request.setParameter("phone_number", user.getPhoneNumber());
        request.setParameter("password", user.getPassword());
        request.setParameter("repeat_password", user.getPassword());
        CommandType.authorizationService = authorizationService;
        when(authorizationService.register(anyObject())).thenReturn(true);
        CommandType.REGISTER.receiveCommand().execute(request);
        assertNull(request.getSession().getAttribute("register_error"));
    }

    @Test
    public void registerValidationTest() throws CommandException, ServiceException {
        request.setParameter("first_name", user.getFirstName());
        request.setParameter("last_name", user.getLastName());
        request.setParameter("middle_name", user.getMiddleName());
        request.setParameter("password", user.getPassword());
        request.setParameter("repeat_password", user.getPassword());
        CommandType.authorizationService = authorizationService;
        when(authorizationService.register(anyObject())).thenReturn(true);
        CommandType.REGISTER.receiveCommand().execute(request);
        assertNotNull(request.getSession().getAttribute("register_error"));
    }

    @Test
    public void showApartmentClassTest() throws CommandException, ServiceException {
        request.setParameter("id", "2");
        CommandType.apartmentClassService = apartmentClassService;
        when(apartmentClassService.findApartmentClassById(2)).thenReturn(Optional.of(apartmentClass));
        CommandType.SHOW_APARTMENT_CLASS.receiveCommand().execute(request);
        assertEquals(request.getAttribute("apartmentClass"), apartmentClass);
    }

    @Test
    public void showApartmentClassErrorTest() throws CommandException, ServiceException {
        CommandType.apartmentClassService = apartmentClassService;
        when(apartmentClassService.findApartmentClassById(anyInt())).thenReturn(Optional.of(apartmentClass));
        CommandType.SHOW_APARTMENT_CLASS.receiveCommand().execute(request);
        assertNotNull(request.getSession().getAttribute("apartment_classes_error"));
    }

    @Test
    public void showPersonaReservationsTest() throws CommandException, ServiceException {
        request.getSession().setAttribute("user", user);
        CommandType.reservationService = reservationService;
        when(reservationService.readAllReservationByUserId(anyLong())).thenReturn(reservations);
        CommandType.SHOW_PERSONAL_RESERVATIONS.receiveCommand().execute(request);
        assertEquals(request.getAttribute("reservations"), reservations);
    }

    @Test
    public void updateProfileTest() throws CommandException, ServiceException {
        String name = "name";
        request.getSession().setAttribute("user", user);
        request.setParameter("first_name", name);
        request.setParameter("last_name", user.getLastName());
        request.setParameter("middle_name", user.getMiddleName());
        request.setParameter("phone_number", user.getPhoneNumber());
        CommandType.userService = userService;
        when(userService.updateUserProfile(anyObject())).thenReturn(true);
        CommandType.UPDATE_PROFILE.receiveCommand().execute(request);
        assertEquals(((User) request.getSession().getAttribute("user")).getFirstName(), name);
    }

    @Test
    public void updateProfileErrorTest() throws CommandException, ServiceException {
        request.getSession().setAttribute("user", user);
        request.setParameter("first_name", "");
        request.setParameter("last_name", user.getLastName());
        request.setParameter("middle_name", user.getMiddleName());
        request.setParameter("phone_number", user.getPhoneNumber());
        CommandType.userService = userService;
        when(userService.updateUserProfile(anyObject())).thenReturn(true);
        CommandType.UPDATE_PROFILE.receiveCommand().execute(request);
        assertNotNull(request.getSession().getAttribute("update_profile_error"));
    }

    @Test
    public void updateUserPasswordTest() throws CommandException, ServiceException {
        String newPassword = "newPassword";
        request.getSession().setAttribute("user", user);
        request.setParameter("current_password", user.getPassword());
        request.setParameter("new_password", newPassword);
        request.setParameter("repeat_new_password", newPassword);
        CommandType.userService = userService;
        when(userService.changeUserPassword(user.getEmail(), user.getPassword(), newPassword)).thenReturn(true);
        user.setPassword(newPassword);
        when(userService.findUserByMail(user.getEmail())).thenReturn(Optional.of(user));
        CommandType.UPDATE_PROFILE.receiveCommand().execute(request);
        assertEquals(((User) request.getSession().getAttribute("user")).getPassword(), newPassword);
    }
}