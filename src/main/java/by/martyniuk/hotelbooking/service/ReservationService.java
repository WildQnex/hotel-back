package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.dao.UserDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import by.martyniuk.hotelbooking.dao.impl.ReservationDaoImpl;
import by.martyniuk.hotelbooking.dao.impl.UserDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.factory.ActionCommandFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class ReservationService {

    private static final Logger LOGGER = LogManager.getLogger(ActionCommandFactory.class);

    public static boolean bookApartment(User user, long apartmentClassId, LocalDate checkInDate, LocalDate checkOutDate, int personsAmount) throws ServiceException {

        boolean result = false;
        try {
            ReservationDao reservationDao = new ReservationDaoImpl();
            ApartmentDao apartmentDao = new ApartmentDaoImpl();
            UserDao userDao = new UserDaoImpl();
            List<Apartment> apartmentList = apartmentDao.findApartmentListByClassId(apartmentClassId);
            Optional<Apartment> apartmentOptional = apartmentList.stream().filter(p -> {
                try {
                    return reservationDao.isApartmentAvailable(p, checkInDate, checkOutDate);
                } catch (DaoException e) {
                    LOGGER.log(Level.ERROR, e);
                    return false;
                }
            }).findFirst();
            if (apartmentOptional.isPresent()) {
                Apartment apartment = apartmentOptional.get();
                BigDecimal totalCost = apartment.getApartmentClass().getCostPerPerson().multiply(new BigDecimal(personsAmount));
                 totalCost = totalCost.add((new BigDecimal(ChronoUnit.DAYS.between(checkInDate, checkOutDate))).multiply(apartment.getApartmentClass().getCostPerNight()));

               BigDecimal newBalance = user.getBalance().subtract(totalCost);
                if (reservationDao.isApartmentAvailable(apartment, checkInDate, checkOutDate)
                        && newBalance.compareTo(new BigDecimal(0)) > 0
                        && apartment.getApartmentClass().getMaxCapacity() >= personsAmount) {
                    result = reservationDao.addReservation(apartment, user, checkInDate, checkOutDate, totalCost, personsAmount);
                }
                if (result) {
                    user.setBalance(newBalance);
                    userDao.updateUser(user);
                }
                return result;
            } else {
                return false;
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }


    public static List<Reservation> readAllReservationByUserId(long userId) throws ServiceException {
        try {
            ReservationDao reservationDao = new ReservationDaoImpl();
            return reservationDao.readAllReservationsByUserId(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public static List<Reservation> readAllReservationByStatus(Status status) throws ServiceException {
        try {
            ReservationDao reservationDao = new ReservationDaoImpl();
            return reservationDao.readAllReservationsByStatus(status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public static boolean updateReservation(Reservation reservation) throws ServiceException {

        return true;
    }

    public static boolean approveReservation(long reservationId, long apartmentId, Status status) throws ServiceException {
        try {
            ReservationDao reservationDao = new ReservationDaoImpl();
            ApartmentDao apartmentDao = new ApartmentDaoImpl();
            Reservation reservation = reservationDao.readReservationById(reservationId);
            Apartment apartment = apartmentDao.findApartmentById(apartmentId);
            if (reservation.getApartment().equals(apartment) || reservationDao.isApartmentAvailable(apartment, reservation.getCheckInDate(),
                    reservation.getCheckOutDate())){
                reservation.setApartment(apartment);
                reservation.setStatus(status);
                return reservationDao.updateReservation(reservation);
            }
            return false;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
