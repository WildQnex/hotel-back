package by.martyniuk.hotelbooking.action;

import by.martyniuk.hotelbooking.constant.CommandConstant;
import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.resource.ResourceManager;
import by.martyniuk.hotelbooking.service.ApartmentClassService;
import by.martyniuk.hotelbooking.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Optional;

/**
 * The Class CommonAction.
 */
@Component
public class CommonAction {


    @Autowired
    private ApartmentClassService apartmentClassService;

    /**
     * Sets the locale.
     *
     * @param request the request
     * @return the string
     */
    public String setLocale(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ResourceManager.currentLocale = Locale.forLanguageTag(request.getParameter(CommandConstant.VALUE).replace('_', '-'));
        session.setAttribute(CommandConstant.LOCALE, request.getParameter(CommandConstant.VALUE));
        request.setAttribute(CommandConstant.REDIRECT, true);
        return request.getHeader(CommandConstant.REFERER);
    }

    /**
     * Forward.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String forward(HttpServletRequest request) throws CommandException {
        if (!PagePath.isPresent(request.getParameter(CommandConstant.PAGE))) {
            throw new CommandException("Page not found");
        }
        return PagePath.valueOf(request.getParameter(CommandConstant.PAGE).toUpperCase()).getPage();
    }

    /**
     * Show apartment class.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String showApartmentClass(HttpServletRequest request) throws CommandException {
        try {
            String stringId = request.getParameter(CommandConstant.ID);
            if (!Validator.validateId(stringId)) {
                request.setAttribute(CommandConstant.REDIRECT, true);
                request.getSession().setAttribute(CommandConstant.APARTMENT_CLASSES_ERROR, ResourceManager.getResourceBundle().getString("error.id"));
                return request.getContextPath() + CommandConstant.SHOW_APARTMENT_CLASSES;
            }
            long id = Long.parseLong(stringId);

            Optional<ApartmentClass> apartmentClassOptional = apartmentClassService.findApartmentClassById(id);
            if (!apartmentClassOptional.isPresent()) {
                request.setAttribute(CommandConstant.REDIRECT, true);
                request.getSession().setAttribute(CommandConstant.APARTMENT_CLASSES_ERROR, ResourceManager.getResourceBundle().getString("error.apartment.class"));
                return request.getContextPath() + CommandConstant.SHOW_APARTMENT_CLASSES;
            }
            request.setAttribute(CommandConstant.APARTMENT_CLASS, apartmentClassOptional.get());

            return PagePath.BOOK_APARTMENT.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Show apartment classes.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String showApartmentClasses(HttpServletRequest request) throws CommandException {
        try {
            request.setAttribute(CommandConstant.APARTMENT_CLASSES, apartmentClassService.findAllApartmentClasses());
            return PagePath.CLASSES.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Command not found.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String commandNotFound(HttpServletRequest request) throws CommandException {
        throw new CommandException("Command operation not found");
    }

}
