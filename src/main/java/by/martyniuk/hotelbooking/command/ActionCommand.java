package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.exception.CommandException;

import javax.servlet.http.HttpServletRequest;

public interface ActionCommand {
    String execute(HttpServletRequest request) throws CommandException;
}
