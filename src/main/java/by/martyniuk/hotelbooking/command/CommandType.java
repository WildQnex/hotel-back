package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.action.AdminAction;
import by.martyniuk.hotelbooking.action.CommonAction;
import by.martyniuk.hotelbooking.action.GuestAction;
import by.martyniuk.hotelbooking.action.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Enum CommandType.
 */
@Component
public class CommandType {

    private AdminAction adminAction;

    private CommonAction commonAction;

    private GuestAction guestAction;

    private UserAction userAction;

    private Map<String, ActionCommand> commands;

    {
        commands = new HashMap<>();
        commands.put("ADD_APARTMENT", request -> adminAction.addApartment(request));
        commands.put("BOOK_APARTMENT", request -> userAction.bookApartment(request));
        commands.put("LOGIN", request -> guestAction.login(request));
        commands.put("LOGOUT", request -> userAction.logout(request));
        commands.put("SET_LOCALE", request -> commonAction.setLocale(request));
        commands.put("FORWARD", request -> commonAction.forward(request));
        commands.put("REGISTER", request -> guestAction.register(request));
        commands.put("SHOW_APARTMENT_CLASS", request -> commonAction.showApartmentClass(request));
        commands.put("SHOW_PERSONAL_RESERVATIONS", request -> userAction.showPersonalReservations(request));
        commands.put("UPDATE_PROFILE", request -> userAction.updateUserProfile(request));
        commands.put("UPDATE_PASSWORD", request -> userAction.updateUserPassword(request));
        commands.put("SHOW_ADMIN_PAGE", request -> adminAction.showAdminPage(request));
        commands.put("SHOW_APARTMENT_CLASSES", request -> commonAction.showApartmentClasses(request));
        commands.put("SHOW_APARTMENT_EDITOR", request -> adminAction.showApartmentEditor(request));
        commands.put("APPROVE_RESERVATION", request -> adminAction.approveReservation(request));
        commands.put("EDIT_APARTMENT", request -> adminAction.editApartment(request));
        commands.put("DEFAULT", request -> commonAction.commandNotFound(request));
        commands.put("SHOW_USER_MANAGER", request -> adminAction.showUserManager(request));
        commands.put("ADD_MONEY", request -> userAction.depositMoney(request));
        commands.put("SHOW_USER_PROFILE", request -> adminAction.showUserProfile(request));
        commands.put("ADMIN_SHOW_USER_PROFILE", request -> adminAction.showUserProfile(request));
        commands.put("ADMIN_SHOW_USER_RESERVATIONS", request -> adminAction.showUserReservations(request));
        commands.put("ADMIN_UPDATE_USER_PROFILE", request -> adminAction.editUserProfile(request));
    }

    @Autowired
    public CommandType(AdminAction adminAction, CommonAction commonAction, GuestAction guestAction, UserAction userAction) {
        this.adminAction = adminAction;
        this.commonAction = commonAction;
        this.guestAction = guestAction;
        this.userAction = userAction;
    }

    /**
     * Receive command.
     *
     * @return the action command
     */
    public ActionCommand receiveCommand(String command) {
        if (!ifPresent(command)) {
            return commands.get("DEFAULT");
        }
        return commands.get(command);
    }

    /**
     * If present.
     *
     * @param command the command
     * @return true, if present
     */
    public boolean ifPresent(String command) {
        return command != null && ((new ArrayList<>(commands.keySet())).contains(command.toUpperCase()));
    }

}
