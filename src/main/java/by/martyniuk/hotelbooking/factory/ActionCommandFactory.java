package by.martyniuk.hotelbooking.factory;

import by.martyniuk.hotelbooking.command.ActionCommand;
import by.martyniuk.hotelbooking.command.CommandType;

public class ActionCommandFactory {
    public static ActionCommand getActionCommand(String action) {
        switch (CommandType.valueOf(action.toUpperCase())) {
            case LOGIN:
                return CommandType.LOGIN.receiveCommand();
            case SET_LOCALE:
                return CommandType.SET_LOCALE.receiveCommand();
            case LOGOUT:
                return CommandType.LOGOUT.receiveCommand();
            case ADD_APARTMENT:
                return CommandType.ADD_APARTMENT.receiveCommand();
            case BOOK_APARTMENT:
                return CommandType.BOOK_APARTMENT.receiveCommand();
            case FORWARD:
                return CommandType.FORWARD.receiveCommand();
            case REGISTER:
                return CommandType.REGISTER.receiveCommand();
            default:
                System.out.println();
                return CommandType.DEFAULT.receiveCommand();
        }
    }
}
