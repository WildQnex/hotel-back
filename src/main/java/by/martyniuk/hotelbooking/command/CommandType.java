package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.service.ApartmentClassService;
import by.martyniuk.hotelbooking.service.ApartmentService;
import by.martyniuk.hotelbooking.service.AuthorizationService;
import by.martyniuk.hotelbooking.service.ReservationService;
import by.martyniuk.hotelbooking.service.UserService;
import by.martyniuk.hotelbooking.service.impl.ApartmentClassServiceImpl;
import by.martyniuk.hotelbooking.service.impl.ApartmentServiceImpl;
import by.martyniuk.hotelbooking.service.impl.AuthorizationServiceImpl;
import by.martyniuk.hotelbooking.service.impl.ReservationServiceImpl;
import by.martyniuk.hotelbooking.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CommandType {

    ADD_APARTMENT(AdminCommands::addApartment),
    BOOK_APARTMENT(UserCommands::bookApartment),
    LOGIN(GuestCommands::login),
    LOGOUT(UserCommands::logout),
    SET_LOCALE(CommonCommands::setLocale),
    FORWARD(CommonCommands::forward),
    REGISTER(GuestCommands::register),
    SHOW_APARTMENT_CLASS(CommonCommands::showApartmentClass),
    SHOW_PERSONAL_RESERVATIONS(UserCommands::showPersonalReservations),
    UPDATE_PROFILE(UserCommands::updateUserProfile),
    UPDATE_PASSWORD(UserCommands::updateUserPassword),
    SHOW_ADMIN_PAGE(AdminCommands::showAdminPage),
    SHOW_APARTMENT_CLASSES(CommonCommands::showApartmentClasses),
    SHOW_APARTMENT_EDITOR(AdminCommands::showApartmentEditor),
    APPROVE_RESERVATION(AdminCommands::approveReservation),
    EDIT_APARTMENT(AdminCommands::editApartment),
    DEFAULT(CommonCommands::commandNotFound),
    SHOW_USER_MANAGER(AdminCommands::showUserManager);

    CommandType(ActionCommand action) {
        this.action = action;
    }

    private static final Logger LOGGER = LogManager.getLogger(CommandType.class);

    private static List<String> commands = Arrays.stream(CommandType.values())
            .map(Enum::toString)
            .collect(Collectors.toList());

    public static ApartmentService apartmentService = new ApartmentServiceImpl();
    public static ApartmentClassService apartmentClassService = new ApartmentClassServiceImpl();
    public static AuthorizationService authorizationService = new AuthorizationServiceImpl();
    public static ReservationService reservationService = new ReservationServiceImpl();
    public static UserService userService = new UserServiceImpl();

    private ActionCommand action;

    public ActionCommand receiveCommand() {
        return action;
    }

    public static boolean ifPresent(String command) {
        return (command != null) && (commands.contains(command.toUpperCase()));
    }

}
