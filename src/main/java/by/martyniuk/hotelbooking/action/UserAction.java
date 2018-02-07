package by.martyniuk.hotelbooking.action;

import by.martyniuk.hotelbooking.command.CommandType;
import by.martyniuk.hotelbooking.constant.CommandConstant;
import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.resource.ResourceManager;
import by.martyniuk.hotelbooking.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * The Class UserAction.
 */
public class UserAction {

    /**
     * Show user profile.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public static String showUserProfile(HttpServletRequest request) throws CommandException {
        try {
            Optional<User> user = CommandType.userService.findUserByMail(((User) request.getSession().getAttribute(CommandConstant.USER)).getEmail());
            user.ifPresent(user1 -> request.getSession().setAttribute(CommandConstant.USER, user1));
            return PagePath.USER.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Book apartment.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public static String bookApartment(HttpServletRequest request) throws CommandException {
        try {
            HttpSession session = request.getSession();
            String checkIn = request.getParameter(CommandConstant.CHECK_IN_DATE);
            String checkOut = request.getParameter(CommandConstant.CHECK_OUT_DATE);
            String stringPersonAmount = request.getParameter(CommandConstant.PERSON_AMOUNT);

            if (!Validator.validatePersonAmount(stringPersonAmount)) {
                session.setAttribute(CommandConstant.BOOKING_ERROR, ResourceManager.getResourceBundle().getString("error.person.amount"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            }

            int personAmount = Integer.parseInt(stringPersonAmount);
            long apartmentClassId = Long.parseLong(request.getParameter(CommandConstant.APARTMENT_CLASS_ID));

            Optional<ApartmentClass> apartmentClass = CommandType.apartmentClassService.findApartmentClassById(apartmentClassId);
            if (apartmentClass.isPresent()) {
                if (apartmentClass.get().getMaxCapacity() < personAmount) {
                    session.setAttribute(CommandConstant.BOOKING_ERROR, ResourceManager.getResourceBundle().getString("error.person.amount"));
                    request.setAttribute(CommandConstant.REDIRECT, true);
                    return request.getHeader(CommandConstant.REFERER);
                }
            } else {
                session.setAttribute(CommandConstant.BOOKING_ERROR, ResourceManager.getResourceBundle().getString("error.apartment.class"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            }

            SimpleDateFormat formatter = new SimpleDateFormat(CommandConstant.DATE_PATTERN, new Locale("en"));
            LocalDate checkInDate = formatter.parse(checkIn).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOutDate = formatter.parse(checkOut).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (!Validator.validateDateRange(checkInDate, checkOutDate)) {
                session.setAttribute(CommandConstant.BOOKING_ERROR, ResourceManager.getResourceBundle().getString("error.date"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            }

            if (!CommandType.reservationService.bookApartment((User) session.getAttribute(CommandConstant.USER), apartmentClassId,
                    checkInDate, checkOutDate, personAmount)) {
                session.setAttribute(CommandConstant.BOOKING_ERROR, ResourceManager.getResourceBundle().getString("error.apartment.booked"));
            } else {
                session.setAttribute(CommandConstant.BOOKING_MESSAGE, ResourceManager.getResourceBundle().getString("message.apartment.booked"));
            }
            request.setAttribute(CommandConstant.REDIRECT, true);
            return request.getHeader(CommandConstant.REFERER);
        } catch (ServiceException | ParseException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Logout.
     *
     * @param request the request
     * @return the string
     */
    public static String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(CommandConstant.USER);
        request.setAttribute(CommandConstant.REDIRECT, true);
        return request.getHeader(CommandConstant.REFERER);
    }

    /**
     * Show personal reservations.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public static String showPersonalReservations(HttpServletRequest request) throws CommandException {
        try {
            List<Reservation> reservations = CommandType.reservationService.readAllReservationByUserId(((User) request.getSession().getAttribute(CommandConstant.USER)).getId());

            request.setAttribute(CommandConstant.RESERVATIONS, reservations);
            return PagePath.USER_RESERVATION.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Deposit money.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public static String depositMoney(HttpServletRequest request) throws CommandException {
        try {
            User user = (User) request.getSession().getAttribute(CommandConstant.USER);

            if (!CommandType.userService.depositMoney(user.getId(), new BigDecimal(request.getParameter(CommandConstant.CURRENCY)))) {
                request.getSession().setAttribute(CommandConstant.UPDATE_PROFILE_ERROR, ResourceManager.getResourceBundle().getString("error.update.balance"));
            }

            request.setAttribute(CommandConstant.REDIRECT, true);
            return request.getHeader(CommandConstant.REFERER);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Update user profile.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public static String updateUserProfile(HttpServletRequest request) throws CommandException {
        try {
            User user = (User) request.getSession().getAttribute(CommandConstant.USER);

            user.setFirstName(request.getParameter(CommandConstant.FIRST_NAME));
            user.setMiddleName(request.getParameter(CommandConstant.MIDDLE_NAME));
            user.setLastName(request.getParameter(CommandConstant.LAST_NAME));
            user.setPhoneNumber(request.getParameter(CommandConstant.PHONE_NUMBER));

            if (!Validator.validateUser(user)) {
                request.getSession().setAttribute(CommandConstant.UPDATE_PROFILE_ERROR, ResourceManager.getResourceBundle().getString("error.personal.data"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            }

            if (CommandType.userService.updateUserProfile(user)) {
                request.getSession().setAttribute(CommandConstant.USER, user);
            }

            request.setAttribute(CommandConstant.REDIRECT, true);
            return request.getHeader(CommandConstant.REFERER);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    /**
     * Update user password.
     *
     * @param request the request
     * @return the string
     * @throws CommandException the command exception
     */
    public static String updateUserPassword(HttpServletRequest request) throws CommandException {
        try {
            String currentPassword = request.getParameter(CommandConstant.CURRENT_PASSWORD);
            String newPassword = request.getParameter(CommandConstant.NEW_PASSWORD);
            String repeatPassword = request.getParameter(CommandConstant.REPEAT_NEW_PASSWORD);

            User user = (User) request.getSession().getAttribute(CommandConstant.USER);

            if (!Validator.validatePasswords(newPassword, repeatPassword)) {
                request.getSession().setAttribute(CommandConstant.UPDATE_PROFILE_ERROR, ResourceManager.getResourceBundle().getString("error.password.not.match"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            }

            if (!CommandType.userService.changeUserPassword(user.getEmail(), currentPassword, newPassword)) {
                request.getSession().setAttribute(CommandConstant.UPDATE_PROFILE_ERROR, ResourceManager.getResourceBundle().getString("error.current.password"));
                request.setAttribute(CommandConstant.REDIRECT, true);
                return request.getHeader(CommandConstant.REFERER);
            }

            CommandType.userService.findUserByMail(user.getEmail())
                    .ifPresent(u -> request.getSession().setAttribute(CommandConstant.USER, u));

            request.setAttribute(CommandConstant.REDIRECT, true);
            return request.getHeader(CommandConstant.REFERER);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
