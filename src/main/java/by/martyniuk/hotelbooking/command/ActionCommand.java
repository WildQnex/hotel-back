package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.exception.CommandException;

import javax.servlet.http.HttpServletRequest;


/**
 * The Interface ActionCommand.
 */
public interface ActionCommand {

    /**
     * Execute.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    String execute(HttpServletRequest request) throws CommandException;
}
