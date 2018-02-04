package by.martyniuk.hotelbooking.action;

import by.martyniuk.hotelbooking.command.CommandType;
import by.martyniuk.hotelbooking.constant.CommandConstant;
import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.resource.ResourceManager;
import by.martyniuk.hotelbooking.util.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.martyniuk.hotelbooking.command.CommandType.apartmentService;

public class AdminAction {
    public static String addApartment(HttpServletRequest request) throws CommandException {
        try {
            String number = request.getParameter(CommandConstant.APARTMENT_NUMBER);
            String apartmentClassId = request.getParameter(CommandConstant.APARTMENT_CLASS);

            int floor = Integer.parseInt(request.getParameter(CommandConstant.APARTMENT_FLOOR));
            Optional<ApartmentClass> apartmentClass = CommandType.apartmentClassService.findApartmentClassById(Long.parseLong(apartmentClassId));
            if (!apartmentClass.isPresent()) {
                request.getSession().setAttribute(CommandConstant.ADD_APARTMENT_ERROR, ResourceManager.getResourceBundle().getString("error.apartment.id"));
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

    public static String showAdminPage(HttpServletRequest request) throws CommandException {
        try {

            List<Reservation> reservations = CommandType.reservationService.readAllReservationByStatus(Status.WAITING_FOR_APPROVE);
            Map<Reservation, List<Apartment>> freeApartments = apartmentService.findFreeApartmentsForReservations(reservations);

            request.setAttribute(CommandConstant.RESERVATIONS, reservations);
            request.setAttribute(CommandConstant.FREE_APARTMENTS, freeApartments);

            return PagePath.APPROVE_RESERVATIONS.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String showApartmentEditor(HttpServletRequest request) throws CommandException {
        try {
            request.setAttribute(CommandConstant.APARTMENTS, apartmentService.findAllApartments());
            request.setAttribute(CommandConstant.APARTMENT_CLASSES, CommandType.apartmentClassService.findAllApartmentClasses());
            return PagePath.APARTMENTS.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String approveReservation(HttpServletRequest request) throws CommandException {
        try {
            if (!Validator.validateId(request.getParameter(CommandConstant.RESERVATION_ID))) {
                request.getSession().setAttribute(CommandConstant.APPROVE_RESERVATION_ERROR, ResourceManager.getResourceBundle().getString("error.apartment.id"));
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

            if (!CommandType.reservationService.updateReservationStatus(reservationId, apartmentId, status)) {
                request.getSession().setAttribute(CommandConstant.APPROVE_RESERVATION_ERROR, ResourceManager.getResourceBundle().getString("error.reservation.approve"));
            }

            request.setAttribute(CommandConstant.REDIRECT, true);
            return request.getContextPath() + CommandConstant.SHOW_ADMIN_PAGE_ACTION;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }

    public static String editApartment(HttpServletRequest request) throws CommandException {
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
                Optional<ApartmentClass> apartmentClass = CommandType.apartmentClassService.findApartmentClassById(Long.parseLong(request.getParameter(CommandConstant.CLASS)));
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

    public static String showUserManager(HttpServletRequest request) throws CommandException {
        try {
            request.setAttribute(CommandConstant.USERS, CommandType.userService.findAllUsers());
            return PagePath.USER_MANAGER.getPage();
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}