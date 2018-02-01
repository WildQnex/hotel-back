package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class CommonCommands {
    public static String setLocale(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        session.setAttribute("locale", request.getParameter("value"));
        request.setAttribute("redirect", true);
        return request.getHeader("referer");
    }

    public static String forward(HttpServletRequest request) throws CommandException {
        if (!PagePath.isPresent(request.getParameter("page"))) {
            throw new CommandException("Page not found");
        }
        return PagePath.valueOf(request.getParameter("page").toUpperCase()).getPage();
    }

    public static String showApartmentClass(HttpServletRequest request) throws CommandException {
        try {
            String stringId = request.getParameter("id");
            if (!Validator.validateId(stringId)) {
                request.setAttribute("redirect", true);
                request.getSession().setAttribute("apartment_classes_error", "Incorrect apartment ID");
                return request.getContextPath() + "/booking?action=show_apartment_classes";
            }
            long id = Long.parseLong(stringId);

            Optional<ApartmentClass> apartmentClassOptional = CommandType.apartmentClassService.findApartmentClassById(id);
            if (!apartmentClassOptional.isPresent()) {
                request.setAttribute("redirect", true);
                request.getSession().setAttribute("apartment_classes_error", "Apartment class not found");
                return request.getContextPath() + "/booking?action=show_apartment_classes";
            }
            request.setAttribute("apartmentClass", apartmentClassOptional.get());

            return PagePath.BOOK_APARTMENT.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String showApartmentClasses(HttpServletRequest request) throws CommandException {
        try {
            request.setAttribute("apartmentClasses", CommandType.apartmentClassService.findAllApartmentClasses());
            return PagePath.CLASSES.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String commandNotFound(HttpServletRequest request) throws CommandException {
        throw new CommandException("Command operation not found");
    }

}
