package by.martyniuk.hotelbooking.service.impl;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import by.martyniuk.hotelbooking.dao.impl.ReservationDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.ApartmentService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApartmentServiceImpl implements ApartmentService {

    private static final Logger LOGGER = LogManager.getLogger(ApartmentServiceImpl.class);

    public static ApartmentDao apartmentDao = new ApartmentDaoImpl();
    public static ReservationDao reservationDao = new ReservationDaoImpl();

    @Override
    public boolean insertApartment(Apartment apartment) throws ServiceException {
        try {
            return apartmentDao.addApartment(apartment);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updateApartment(Apartment apartment) throws ServiceException {
        try {
            return apartmentDao.updateApartment(apartment);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean deleteApartment(long apartmentId) throws ServiceException {
        try {
            return apartmentDao.deleteApartment(apartmentId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<Apartment> getApartment(long id) throws ServiceException {
        try {
            return apartmentDao.findApartmentById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Map<Reservation, List<Apartment>> findFreeApartmentsForReservations(List<Reservation> reservations) throws ServiceException {
        Map<Reservation, List<Apartment>> freeApartments = new HashMap<>();
        System.out.println(reservations);
        try {
            for (Reservation reservation : reservations) {
                List<Apartment> apartments = apartmentDao.findApartmentListByClassId(reservation.getApartment().getApartmentClass().getId());
                System.out.println(apartments);
                freeApartments.put(reservation, apartments.stream()
                        .filter(apartment -> {
                            try {
                                System.out.println(apartment);
                                return reservationDao.isApartmentAvailable(apartment.getId(), reservation.getCheckInDate(), reservation.getCheckOutDate());
                            } catch (DaoException e) {
                                LOGGER.log(Level.ERROR, e);
                            }
                            return false;
                        })
                        .collect(Collectors.toList()));
            }
            return freeApartments;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Apartment> findAllApartments() throws ServiceException {
        try {
            return apartmentDao.findAllApartments();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}