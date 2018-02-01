package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class UserCommands {
    public static String bookApartment(HttpServletRequest request) throws CommandException {
        try {
            String pattern = "dd MMMMM, yyyy";
            HttpSession session = request.getSession();
            String checkIn = request.getParameter("check_in_date");
            String checkOut = request.getParameter("check_out_date");
            String stringPersonAmount = request.getParameter("person_amount");

            if (!Validator.validatePersonAmount(stringPersonAmount)) {
                session.setAttribute("booking_error", "Incorrect person amount");
                request.setAttribute("redirect", true);
                return request.getHeader("referer");
            }

            int personAmount = Integer.parseInt(stringPersonAmount);
            long apartmentClassId = Long.parseLong(request.getParameter("apartment_id"));

            Optional<ApartmentClass> apartmentClass = CommandType.apartmentClassService.findApartmentClassById(apartmentClassId);
            if (apartmentClass.isPresent()) {
                if (apartmentClass.get().getMaxCapacity() < personAmount) {
                    session.setAttribute("booking_error", "Incorrect person amount");
                    request.setAttribute("redirect", true);
                    return request.getHeader("referer");
                }
            } else {
                session.setAttribute("booking_error", "Incorrect apartment class");
                request.setAttribute("redirect", true);
                return request.getHeader("referer");
            }

            SimpleDateFormat formatter = new SimpleDateFormat(pattern, new Locale("en"));
            LocalDate checkInDate = formatter.parse(checkIn).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOutDate = formatter.parse(checkOut).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (!Validator.validateDateRange(checkInDate, checkOutDate)) {
                session.setAttribute("booking_error", "Incorrect date");
                request.setAttribute("redirect", true);
                return request.getHeader("referer");
            }

            if (!CommandType.reservationService.bookApartment((User) session.getAttribute("user"), apartmentClassId,
                    checkInDate, checkOutDate, personAmount)) {
                session.setAttribute("booking_error", "Apartment already booked");
            } else {
                session.setAttribute("booking_error", "Apartment booked successfully");
            }
            request.setAttribute("redirect", true);
            return request.getHeader("referer");
        } catch (ServiceException | ParseException e) {
            throw new CommandException(e);
        }
    }

    public static String logout(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        request.setAttribute("redirect", true);
        return request.getHeader("referer");
    }

    public static String showPersonalReservations(HttpServletRequest request) throws CommandException {
        try {
            List<Reservation> reservations = CommandType.reservationService.readAllReservationByUserId(((User) request.getSession().getAttribute("user")).getId());

            request.setAttribute("reservations", reservations);
            return PagePath.USER_RESERVATION.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String updateUserProfile(HttpServletRequest request) throws CommandException {
        try {
            User user = (User) request.getSession().getAttribute("user");

            user.setFirstName(request.getParameter("first_name"));
            user.setMiddleName(request.getParameter("middle_name"));
            user.setLastName(request.getParameter("last_name"));
            user.setPhoneNumber(request.getParameter("phone_number"));

            if (!Validator.validateUser(user)) {
                request.getSession().setAttribute("update_profile_error", "Incorrect personal data");
                request.setAttribute("redirect", true);
                return request.getHeader("referer");
            }

            if (CommandType.userService.updateUserProfile(user)) {
                request.getSession().setAttribute("user", user);
            }

            request.setAttribute("redirect", true);
            return request.getHeader("referer");
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String updateUserPassword(HttpServletRequest request) throws CommandException {
        try {
            String currentPassword = request.getParameter("current_password");
            String newPassword = request.getParameter("new_password");
            String repeatPassword = request.getParameter("repeat_new_password");

            User user = (User) request.getSession().getAttribute("user");

            if (!Validator.validatePasswords(newPassword, repeatPassword)) {
                request.getSession().setAttribute("update_profile_error", "Passwords do not match");
                request.setAttribute("redirect", true);
                return request.getHeader("referer");
            }

            if (!CommandType.userService.changeUserPassword(user.getEmail(), currentPassword, newPassword)) {
                request.getSession().setAttribute("update_profile_error", "Incorrect current password");
                request.setAttribute("redirect", true);
                return request.getHeader("referer");
            }

            CommandType.userService.findUserByMail(user.getEmail())
                    .ifPresent(u -> request.getSession().setAttribute("user", u));

            request.setAttribute("redirect", true);
            return request.getHeader("referer");
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
