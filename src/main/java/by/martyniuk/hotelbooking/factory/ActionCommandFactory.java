package by.martyniuk.hotelbooking.factory;

import by.martyniuk.hotelbooking.command.ActionCommand;
import by.martyniuk.hotelbooking.command.CommandType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActionCommandFactory {

    private static final Logger LOGGER = LogManager.getLogger(ActionCommandFactory.class);

    public static ActionCommand getActionCommand(String action) {
        try {
            CommandType command = CommandType.valueOf(action.toUpperCase());
            return command.receiveCommand();
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return CommandType.DEFAULT.receiveCommand();
    }
}
