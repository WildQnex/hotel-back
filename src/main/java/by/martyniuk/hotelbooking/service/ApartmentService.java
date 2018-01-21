package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.command.CommandType;
import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import by.martyniuk.hotelbooking.dao.impl.ReservationDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApartmentService {

    private static final Logger LOGGER = LogManager.getLogger(ApartmentService.class);

    public static Apartment getApartment(long id) throws ServiceException {
        try {
            ApartmentDao dao = new ApartmentDaoImpl();
            return dao.findApartmentById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public static Map<Reservation, List<Apartment>> findFreeApartmentsForReservations(List<Reservation> reservations) throws ServiceException {

        Map<Reservation, List<Apartment>> freeApartments = new HashMap<>();
        ApartmentDao apartmentDao = new ApartmentDaoImpl();
        ReservationDao reservationDao = new ReservationDaoImpl();
        try {


            for (Reservation reservation : reservations) {
                List<Apartment> apartments = apartmentDao.findApartmentListByClassId(reservation.getApartment().getApartmentClass().getId());
                freeApartments.put(reservation, apartments.stream()
                        .filter(apartment -> {
                            try {
                                return reservationDao.isApartmentAvailable(apartment, reservation.getCheckInDate(), reservation.getCheckOutDate());
                            } catch (DaoException e) {
                                LOGGER.log(Level.ERROR, e);
                            }
                            return false;
                        })
                        .collect(Collectors.toList()));
            }
            return freeApartments;
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }
}