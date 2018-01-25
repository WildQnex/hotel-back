package by.martyniuk.hotelbooking.factory;

import by.martyniuk.hotelbooking.command.ActionCommand;
import by.martyniuk.hotelbooking.command.CommandType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActionCommandFactory {

    private static final Logger LOGGER = LogManager.getLogger(ActionCommandFactory.class);

    public static ActionCommand getActionCommand(String action) {
        if (CommandType.ifPresent(action)) {
            CommandType command = CommandType.valueOf(action.toUpperCase());
            return command.receiveCommand();
        }
        return CommandType.DEFAULT.receiveCommand();
    }
}
