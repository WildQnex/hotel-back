package by.martyniuk.hotelbooking.service.impl;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import by.martyniuk.hotelbooking.dao.impl.ReservationDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.factory.ActionCommandFactory;
import by.martyniuk.hotelbooking.service.ReservationService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * The Class ReservationServiceImpl.
 */
public class ReservationServiceImpl implements ReservationService {

    /**
     * The reservation dao.
     */
    public static ReservationDao reservationDao = new ReservationDaoImpl();

    /**
     * The apartment dao.
     */
    public static ApartmentDao apartmentDao = new ApartmentDaoImpl();

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LogManager.getLogger(ActionCommandFactory.class);


    @Override
    public boolean bookApartment(User user, long apartmentClassId, LocalDate checkInDate, LocalDate checkOutDate, int personsAmount) throws ServiceException {
        try {
            List<Apartment> apartmentList = apartmentDao.findApartmentListByClassId(apartmentClassId);
            Optional<Apartment> apartmentOptional = apartmentList.stream().filter(p -> {
                try {
                    return reservationDao.isApartmentAvailable(p.getId(), checkInDate, checkOutDate);
                } catch (DaoException e) {
                    LOGGER.log(Level.ERROR, e);
                    return false;
                }
            }).findFirst();
            if (apartmentOptional.isPresent()) {
                Apartment apartment = apartmentOptional.get();
                long daysAmount = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
                BigDecimal totalCost = apartment.getApartmentClass().getCostPerPerson()
                        .multiply(new BigDecimal(personsAmount))
                        .multiply(new BigDecimal(daysAmount));
                totalCost = totalCost.add((new BigDecimal(daysAmount))
                        .multiply(apartment.getApartmentClass().getCostPerNight()));

                return reservationDao.addReservation(apartmentOptional.get(), user, checkInDate, checkOutDate, totalCost, personsAmount);
            }
            return false;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Reservation> readAllReservationByUserId(long userId) throws ServiceException {
        try {
            return reservationDao.readAllReservationsByUserId(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Reservation> readAllReservationByStatus(Status status) throws ServiceException {
        try {
            return reservationDao.readAllReservationsByStatus(status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updateReservationStatus(long reservationId, long apartmentId, Status status) throws ServiceException {
        try {
            Optional<Reservation> reservationOptional = reservationDao.readReservationById(reservationId);

            if (!reservationOptional.isPresent()) {
                return false;
            }
            Reservation reservation = reservationOptional.get();
            if (!reservation.getStatus().equals(Status.WAITING_FOR_APPROVE)) {
                return false;
            }
            Optional<Apartment> apartmentOptional = apartmentDao.findApartmentById(apartmentId);

            if (!apartmentOptional.isPresent()) {
                return false;
            }
            Apartment apartment = apartmentOptional.get();
            if (reservation.getApartment().equals(apartment) || (reservationDao.isApartmentAvailable(apartmentId, reservation.getCheckInDate(),
                    reservation.getCheckOutDate()) && apartment.getApartmentClass().equals(reservation.getApartment().getApartmentClass()))) {

                reservation.setApartment(apartment);

                return reservationDao.updateReservationApartmentAndStatus(reservation, status);
            }
            return false;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
