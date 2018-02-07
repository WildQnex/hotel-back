package by.martyniuk.hotelbooking.action;

import by.martyniuk.hotelbooking.command.CommandType;
import by.martyniuk.hotelbooking.constant.CommandConstant;
import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.resource.ResourceManager;
import by.martyniuk.hotelbooking.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * The Class GuestAction.
 */
public class GuestAction {

    /**
     * Login.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public static String login(HttpServletRequest request) throws CommandException {
        try {
            HttpSession session = request.getSession();
            String mail = request.getParameter(CommandConstant.EMAIL);
            String password = request.getParameter(CommandConstant.PASSWORD);
            Optional<User> user = CommandType.authorizationService.login(mail, password);
            if (user.isPresent()) {
                session.setAttribute(CommandConstant.USER, user.get());
            } else {
                session.setAttribute(CommandConstant.LOGIN_ERROR, ResourceManager.getResourceBundle().getString("error.login.or.password"));
            }
            request.setAttribute(CommandConstant.REDIRECT, true);
            return request.getHeader(CommandConstant.REFERER);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Register.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public static String register(HttpServletRequest request) throws CommandException {
        try {
            User user = new User();
            String password = request.getParameter(CommandConstant.PASSWORD);
            String repeatPassword = request.getParameter(CommandConstant.REPEAT_PASSWORD);
            user.setPassword(password);
            user.setMiddleName(request.getParameter(CommandConstant.MIDDLE_NAME));
            user.setFirstName(request.getParameter(CommandConstant.FIRST_NAME));
            user.setLastName(request.getParameter(CommandConstant.LAST_NAME));
            user.setEmail(request.getParameter(CommandConstant.EMAIL));
            user.setPhoneNumber(request.getParameter(CommandConstant.PHONE_NUMBER));
            System.out.println(user);
            if (!(Validator.validatePasswords(password, repeatPassword) && Validator.validateUser(user))) {
                request.getSession().setAttribute(CommandConstant.REGISTER_ERROR, ResourceManager.getResourceBundle().getString("error.personal.data"));
                return PagePath.REGISTER.getPage();
            }

            if (CommandType.authorizationService.register(user)) {
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getContextPath() + CommandConstant.INDEX;
            }

            request.setAttribute(CommandConstant.REDIRECT, true);
            request.getSession().setAttribute(CommandConstant.REGISTER_ERROR, ResourceManager.getResourceBundle().getString("error.account.exist"));
            return request.getHeader(CommandConstant.REFERER);

        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
