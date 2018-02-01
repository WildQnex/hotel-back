package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.util.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.martyniuk.hotelbooking.command.CommandType.apartmentService;

public class AdminCommands {
    public static String addApartment(HttpServletRequest request) throws CommandException {
        try {
            String number = request.getParameter("apartmentNumber");
            String apartmentClassId = request.getParameter("apartmentClass");

            int floor = Integer.parseInt(request.getParameter("apartmentFloor"));
            Optional<ApartmentClass> apartmentClass = CommandType.apartmentClassService.findApartmentClassById(Long.parseLong(apartmentClassId));
            if (!apartmentClass.isPresent()) {
                request.getSession().setAttribute("add_apartment_error", "Apartment class not found");
                request.setAttribute("redirect", true);
                return request.getHeader("referer");
            }
            Apartment apartment = new Apartment(0, number, floor, apartmentClass.get(), true);
            apartmentService.insertApartment(apartment);
            request.setAttribute("redirect", true);
            return request.getHeader("referer");
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String showAdminPage(HttpServletRequest request) throws CommandException {
        try {

            List<Reservation> reservations = CommandType.reservationService.readAllReservationByStatus(Status.WAITING_FOR_APPROVE);
            Map<Reservation, List<Apartment>> freeApartments = apartmentService.findFreeApartmentsForReservations(reservations);

            request.setAttribute("reservations", reservations);
            request.setAttribute("freeApartments", freeApartments);

            return PagePath.APPROVE_RESERVATIONS.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String showApartmentEditor(HttpServletRequest request) throws CommandException {
        try {
            request.setAttribute("apartments", apartmentService.findAllApartments());
            request.setAttribute("apartmentClasses", CommandType.apartmentClassService.findAllApartmentClasses());
            return PagePath.APARTMENTS.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String approveReservation(HttpServletRequest request) throws CommandException {
        try {
            if (!Validator.validateId(request.getParameter("reservation_id"))) {
                request.getSession().setAttribute("approve_reservation_error", "Incorrect reservation Id");
                request.setAttribute("redirect", true);
                return request.getContextPath() + "/booking?action=show_admin_page";
            }
            System.out.println(request.getParameter("apartment_id"));
            if (!Validator.validateId(request.getParameter("apartment_id"))) {
                request.getSession().setAttribute("approve_reservation_error", "Incorrect apartment Id");
                request.setAttribute("redirect", true);
                return request.getContextPath() + "/booking?action=show_admin_page";
            }

            if (!Validator.validateStatus(request.getParameter("status"))) {
                request.getSession().setAttribute("approve_reservation_error", "Incorrect reservation status");
                request.setAttribute("redirect", true);
                return request.getContextPath() + "/booking?action=show_admin_page";
            }

            long reservationId = Long.parseLong(request.getParameter("reservation_id"));
            long apartmentId = Long.parseLong(request.getParameter("apartment_id"));
            Status status = Status.valueOf(request.getParameter("status").toUpperCase());

            if (!CommandType.reservationService.updateReservationStatus(reservationId, apartmentId, status)) {
                request.getSession().setAttribute("approve_reservation_error", "Reservation not approved");
            }

            request.setAttribute("redirect", true);
            return request.getContextPath() + "/booking?action=show_admin_page";
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String editApartment(HttpServletRequest request) throws CommandException {
        try {
            boolean result = false;
            if (request.getParameter("type").equalsIgnoreCase("delete")) {
                result = apartmentService.deleteApartment(Long.parseLong(request.getParameter("apartmentId")));
            } else if (request.getParameter("type").equalsIgnoreCase("update")) {
                Optional<Apartment> apartmentOptional = apartmentService.getApartment(Long.parseLong(request.getParameter("apartmentId")));
                String number = request.getParameter("number");
                if (!apartmentOptional.isPresent()) {
                    request.setAttribute("redirect", true);
                    return request.getHeader("referer");
                }
                Apartment apartment = apartmentOptional.get();
                apartment.setNumber(number);
                Optional<ApartmentClass> apartmentClass = CommandType.apartmentClassService.findApartmentClassById(Long.parseLong(request.getParameter("class")));
                if (!apartmentClass.isPresent()) {
                    request.getSession().setAttribute("edit_apartment_error", "Apartment class not found");
                    request.setAttribute("redirect", true);
                    return request.getHeader("referer");
                }
                apartment.setApartmentClass(apartmentClass.get());
                int floor = Integer.parseInt(request.getParameter("floor"));
                apartment.setFloor(floor);
                result = apartmentService.updateApartment(apartment);
            }
            if (result) {
                request.setAttribute("redirect", true);
                return request.getHeader("referer");
            } else {
                request.setAttribute("redirect", true);
                return request.getHeader("referer");
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String showUserManager(HttpServletRequest request) throws CommandException {
        try {
            request.setAttribute("users", CommandType.userService.findAllUsers());
            return PagePath.USER_MANAGER.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
