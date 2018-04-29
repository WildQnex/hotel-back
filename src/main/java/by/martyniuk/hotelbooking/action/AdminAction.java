package by.martyniuk.hotelbooking.action;

import by.martyniuk.hotelbooking.constant.CommandConstant;
import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.entity.*;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.resource.ResourceManager;
import by.martyniuk.hotelbooking.service.ApartmentClassService;
import by.martyniuk.hotelbooking.service.ApartmentService;
import by.martyniuk.hotelbooking.service.ReservationService;
import by.martyniuk.hotelbooking.service.UserService;
import by.martyniuk.hotelbooking.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The Class AdminAction.
 */
@Component
public class AdminAction {

    @Autowired
    private ApartmentClassService apartmentClassService;

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    /**
     * Adds the apartment.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String addApartment(HttpServletRequest request) throws CommandException {
        try {
            String number = request.getParameter(CommandConstant.APARTMENT_NUMBER);
            String apartmentClassId = request.getParameter(CommandConstant.APARTMENT_CLASS);

            int floor = Integer.parseInt(request.getParameter(CommandConstant.APARTMENT_FLOOR));
            Optional<ApartmentClass> apartmentClass = apartmentClassService.findApartmentClassById(Long.parseLong(apartmentClassId));
            if (!apartmentClass.isPresent()) {
                request.getSession().setAttribute(CommandConstant.ADD_APARTMENT_ERROR, ResourceManager.getResourceBundle().getString("error.incorrect.apartment.id"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            }
            Apartment apartment = new Apartment(0, number, floor, apartmentClass.get(), true);
            apartmentService.insertApartment(apartment);
            request.setAttribute(CommandConstant.REDIRECT, true);
            return request.getHeader(CommandConstant.REFERER);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Shows the user profile.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String showUserProfile(HttpServletRequest request) throws CommandException {
        try {
            String stringId = request.getParameter(CommandConstant.ID);
            if (Validator.validateId(stringId)) {
                Optional<User> user = userService.findUserById(Long.parseLong(stringId));
                user.ifPresent(u -> request.setAttribute(CommandConstant.USER_PROFILE, u));
            }
            return PagePath.ADMIN_USER_PROFILE.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Edit the user profile.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String editUserProfile(HttpServletRequest request) throws CommandException {
        try {
            String stringId = request.getParameter(CommandConstant.ID);

            if (!Validator.validateId(stringId)) {
                request.getSession().setAttribute(CommandConstant.UPDATE_PROFILE_ERROR, ResourceManager.getResourceBundle().getString("error.user.id"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            }

            Optional<User> optionalUser = userService.findUserById(Long.parseLong(stringId));

            if (!optionalUser.isPresent()) {
                request.getSession().setAttribute(CommandConstant.UPDATE_PROFILE_ERROR, ResourceManager.getResourceBundle().getString("error.user.not.found"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            }

            User user = optionalUser.get();

            user.setFirstName(request.getParameter(CommandConstant.FIRST_NAME));
            user.setMiddleName(request.getParameter(CommandConstant.MIDDLE_NAME));
            user.setLastName(request.getParameter(CommandConstant.LAST_NAME));
            user.setPhoneNumber(request.getParameter(CommandConstant.PHONE_NUMBER));

            String role = request.getParameter(CommandConstant.ROLE);

            if (!(Validator.validateUser(user) && Validator.validateRole(role))) {
                request.getSession().setAttribute(CommandConstant.UPDATE_PROFILE_ERROR, ResourceManager.getResourceBundle().getString("error.personal.data"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            }

            user.setRole(Role.valueOf(role.toUpperCase()));
            user.setActive(Boolean.parseBoolean(request.getParameter(CommandConstant.ACTIVE)));

            if (!userService.updateUserProfile(user)) {
                request.getSession().setAttribute(CommandConstant.UPDATE_PROFILE_ERROR, ResourceManager.getResourceBundle().getString("error.personal.data"));
            }

            request.setAttribute(CommandConstant.REDIRECT, true);
            return request.getHeader(CommandConstant.REFERER);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Show user reservations.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String showUserReservations(HttpServletRequest request) throws CommandException {
        try {
            String stringId = request.getParameter(CommandConstant.ID);
            if (Validator.validateId(stringId)) {
                List<Reservation> reservations = reservationService.readAllReservationByUserId(Long.parseLong(stringId));
                request.setAttribute(CommandConstant.RESERVATIONS, reservations);
            }
            return PagePath.USER_RESERVATION.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Show admin page.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String showAdminPage(HttpServletRequest request) throws CommandException {
        try {

            List<Reservation> reservations = reservationService.readAllReservationByStatus(Status.WAITING_FOR_APPROVE);
            Map<Reservation, List<Apartment>> freeApartments = apartmentService.findFreeApartmentsForReservations(reservations);

            request.setAttribute(CommandConstant.RESERVATIONS, reservations);
            request.setAttribute(CommandConstant.FREE_APARTMENTS, freeApartments);

            return PagePath.APPROVE_RESERVATIONS.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Show apartment editor.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String showApartmentEditor(HttpServletRequest request) throws CommandException {
        try {
            request.setAttribute(CommandConstant.APARTMENTS, apartmentService.findAllApartments());
            request.setAttribute(CommandConstant.APARTMENT_CLASSES, apartmentClassService.findAllApartmentClasses());
            return PagePath.APARTMENTS.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Approve reservation.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String approveReservation(HttpServletRequest request) throws CommandException {
        try {
            if (!Validator.validateId(request.getParameter(CommandConstant.RESERVATION_ID))) {
                request.getSession().setAttribute(CommandConstant.APPROVE_RESERVATION_ERROR, ResourceManager.getResourceBundle().getString("error.incorrect.apartment.id"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getContextPath() + CommandConstant.SHOW_ADMIN_PAGE_ACTION;
            }

            if (!Validator.validateStatus(request.getParameter(CommandConstant.STATUS))) {
                request.getSession().setAttribute(CommandConstant.APPROVE_RESERVATION_ERROR, ResourceManager.getResourceBundle().getString("error.reservation.status"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getContextPath() + CommandConstant.SHOW_ADMIN_PAGE_ACTION;
            }

            long reservationId = Long.parseLong(request.getParameter(CommandConstant.RESERVATION_ID));
            long apartmentId = Long.parseLong(request.getParameter(CommandConstant.APARTMENT_ID));
            Status status = Status.valueOf(request.getParameter(CommandConstant.STATUS).toUpperCase());

            if (!reservationService.updateReservationStatus(reservationId, apartmentId, status)) {
                request.getSession().setAttribute(CommandConstant.APPROVE_RESERVATION_ERROR, ResourceManager.getResourceBundle().getString("error.reservation.approve"));
            }

            request.setAttribute(CommandConstant.REDIRECT, true);
            return request.getContextPath() + CommandConstant.SHOW_ADMIN_PAGE_ACTION;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Edits the apartment.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String editApartment(HttpServletRequest request) throws CommandException {
        try {
            boolean result = false;
            if (request.getParameter(CommandConstant.TYPE).equalsIgnoreCase(CommandConstant.DELETE)) {
                result = apartmentService.deleteApartment(Long.parseLong(request.getParameter(CommandConstant.APARTMENT_ID)));
            } else if (request.getParameter(CommandConstant.TYPE).equalsIgnoreCase(CommandConstant.UPDATE)) {
                Optional<Apartment> apartmentOptional = apartmentService.getApartment(Long.parseLong(request.getParameter(CommandConstant.APARTMENT_ID)));
                String number = request.getParameter(CommandConstant.NUMBER);
                if (!apartmentOptional.isPresent()) {
                    request.setAttribute(CommandConstant.REDIRECT, true);
                    return request.getHeader(CommandConstant.REFERER);
                }
                Apartment apartment = apartmentOptional.get();
                apartment.setNumber(number);
                Optional<ApartmentClass> apartmentClass = apartmentClassService.findApartmentClassById(Long.parseLong(request.getParameter(CommandConstant.CLASS)));
                if (!apartmentClass.isPresent()) {
                    request.getSession().setAttribute(CommandConstant.EDIT_APARTMENT_ERROR, ResourceManager.getResourceBundle().getString("error.apartment.class"));
                    request.setAttribute(CommandConstant.REDIRECT, true);
                    return request.getHeader(CommandConstant.REFERER);
                }
                apartment.setApartmentClass(apartmentClass.get());
                int floor = Integer.parseInt(request.getParameter(CommandConstant.FLOOR));
                apartment.setFloor(floor);
                result = apartmentService.updateApartment(apartment);
            }
            if (result) {
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            } else {
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Show user manager.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public String showUserManager(HttpServletRequest request) throws CommandException {
        try {
            request.setAttribute(CommandConstant.USERS, userService.findAllUsers());
            return PagePath.USER_MANAGER.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
