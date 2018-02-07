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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The Enum CommandType.
 */
public enum CommandType {

    /**
     * The add apartment.
     */
    ADD_APARTMENT(AdminAction::addApartment),

    /**
     * The book apartment.
     */
    BOOK_APARTMENT(UserAction::bookApartment),

    /**
     * The login.
     */
    LOGIN(GuestAction::login),

    /**
     * The logout.
     */
    LOGOUT(UserAction::logout),

    /**
     * The set locale.
     */
    SET_LOCALE(CommonAction::setLocale),

    /**
     * The forward.
     */
    FORWARD(CommonAction::forward),

    /**
     * The register.
     */
    REGISTER(GuestAction::register),

    /**
     * The show apartment class.
     */
    SHOW_APARTMENT_CLASS(CommonAction::showApartmentClass),

    /**
     * The show personal reservations.
     */
    SHOW_PERSONAL_RESERVATIONS(UserAction::showPersonalReservations),

    /**
     * The update profile.
     */
    UPDATE_PROFILE(UserAction::updateUserProfile),

    /**
     * The update password.
     */
    UPDATE_PASSWORD(UserAction::updateUserPassword),

    /**
     * The show admin page.
     */
    SHOW_ADMIN_PAGE(AdminAction::showAdminPage),

    /**
     * The show apartment classes.
     */
    SHOW_APARTMENT_CLASSES(CommonAction::showApartmentClasses),

    /**
     * The show apartment editor.
     */
    SHOW_APARTMENT_EDITOR(AdminAction::showApartmentEditor),

    /**
     * The approve reservation.
     */
    APPROVE_RESERVATION(AdminAction::approveReservation),

    /**
     * The edit apartment.
     */
    EDIT_APARTMENT(AdminAction::editApartment),

    /**
     * The default.
     */
    DEFAULT(CommonAction::commandNotFound),

    /**
     * The show user manager.
     */
    SHOW_USER_MANAGER(AdminAction::showUserManager),

    /**
     * The add money.
     */
    ADD_MONEY(UserAction::depositMoney),

    /**
     * The show user profile.
     */
    SHOW_USER_PROFILE(UserAction::showUserProfile),

    /**
     * The show user profile to admin.
     */
    ADMIN_SHOW_USER_PROFILE(AdminAction::showUserProfile),

    /**
     * The show user reservations to admin.
     */
    ADMIN_SHOW_USER_RESERVATIONS(AdminAction::showUserReservations),

    ADMIN_UPDATE_USER_PROFILE(AdminAction::editUserProfile);

    /**
     * Instantiates a new command type.
     *
     * @param action the action
     */
    CommandType(ActionCommand action) {
        this.action = action;
    }

    /**
     * The commands.
     */
    private static List<String> commands = Arrays.stream(CommandType.values())
            .map(Enum::toString)
            .collect(Collectors.toList());

    /**
     * The apartment service.
     */
    public static ApartmentService apartmentService = new ApartmentServiceImpl();

    /**
     * The apartment class service.
     */
    public static ApartmentClassService apartmentClassService = new ApartmentClassServiceImpl();

    /**
     * The authorization service.
     */
    public static AuthorizationService authorizationService = new AuthorizationServiceImpl();

    /**
     * The reservation service.
     */
    public static ReservationService reservationService = new ReservationServiceImpl();

    /**
     * The user service.
     */
    public static UserService userService = new UserServiceImpl();

    /**
     * The action.
     */
    private ActionCommand action;

    /**
     * Receive command.
     *
     * @return the action command
     */
    public ActionCommand receiveCommand() {
        return action;
    }

    /**
     * If present.
     *
     * @param command the command
     * @return true, if present
     */
    public static boolean ifPresent(String command) {
        return command != null && (commands.contains(command.toUpperCase()));
    }

}
