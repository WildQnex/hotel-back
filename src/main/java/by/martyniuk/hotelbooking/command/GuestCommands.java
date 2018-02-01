package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class GuestCommands {
    public static String login(HttpServletRequest request) throws CommandException {
        try {
            HttpSession session = request.getSession();
            String mail = request.getParameter("email");
            String password = request.getParameter("password");
            Optional<User> user = CommandType.authorizationService.login(mail, password);
            if (user.isPresent()) {
                session.setAttribute("user", user.get());
            } else {
                session.setAttribute("login_error", "Invalid e-mail or password");
            }
            request.setAttribute("redirect", true);
            return request.getHeader("referer");
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String register(HttpServletRequest request) throws CommandException {
        try {
            User user = new User();
            String password = request.getParameter("password");
            String repeatPassword = request.getParameter("repeat_password");
            user.setPassword(password);
            user.setMiddleName(request.getParameter("middle_name"));
            user.setFirstName(request.getParameter("first_name"));
            user.setLastName(request.getParameter("last_name"));
            user.setEmail(request.getParameter("email"));
            user.setPhoneNumber(request.getParameter("phone_number"));
            System.out.println(user);
            if (!(Validator.validatePasswords(password, repeatPassword) && Validator.validateUser(user))) {
                request.getSession().setAttribute("register_error", "Incorrect personal data");
                return PagePath.REGISTER.getPage();
            }

            if (CommandType.authorizationService.register(user)) {
                request.setAttribute("redirect", true);
                return request.getContextPath() + "/index.jsp";
            }

            request.setAttribute("redirect", true);
            request.getSession().setAttribute("register_error", "Account with this e-mail already exists");
            return request.getHeader("referer");

        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
