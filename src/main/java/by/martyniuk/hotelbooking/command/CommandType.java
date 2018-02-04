package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.action.AdminAction;
import by.martyniuk.hotelbooking.action.CommonAction;
import by.martyniuk.hotelbooking.action.GuestAction;
import by.martyniuk.hotelbooking.action.UserAction;
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

    ADD_APARTMENT(AdminAction::addApartment),
    BOOK_APARTMENT(UserAction::bookApartment),
    LOGIN(GuestAction::login),
    LOGOUT(UserAction::logout),
    SET_LOCALE(CommonAction::setLocale),
    FORWARD(CommonAction::forward),
    REGISTER(GuestAction::register),
    SHOW_APARTMENT_CLASS(CommonAction::showApartmentClass),
    SHOW_PERSONAL_RESERVATIONS(UserAction::showPersonalReservations),
    UPDATE_PROFILE(UserAction::updateUserProfile),
    UPDATE_PASSWORD(UserAction::updateUserPassword),
    SHOW_ADMIN_PAGE(AdminAction::showAdminPage),
    SHOW_APARTMENT_CLASSES(CommonAction::showApartmentClasses),
    SHOW_APARTMENT_EDITOR(AdminAction::showApartmentEditor),
    APPROVE_RESERVATION(AdminAction::approveReservation),
    EDIT_APARTMENT(AdminAction::editApartment),
    DEFAULT(CommonAction::commandNotFound),
    SHOW_USER_MANAGER(AdminAction::showUserManager);

    CommandType(ActionCommand action) {
        this.action = action;
    }

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
        return command != null && (commands.contains(command.toUpperCase()));
    }

}
