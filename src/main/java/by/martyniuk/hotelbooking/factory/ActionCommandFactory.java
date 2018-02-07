package by.martyniuk.hotelbooking.factory;

import by.martyniuk.hotelbooking.command.ActionCommand;
import by.martyniuk.hotelbooking.command.CommandType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A factory for creating ActionCommand objects.
 */
public class ActionCommandFactory {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LogManager.getLogger(ActionCommandFactory.class);

    /**
     * Gets the action command.
     *
     * @param action the action
     * @return the action command
     */
    public static ActionCommand getActionCommand(String action) {
        if (CommandType.ifPresent(action)) {
            CommandType command = CommandType.valueOf(action.toUpperCase());
            return command.receiveCommand();
        }
        return CommandType.DEFAULT.receiveCommand();
    }
}
