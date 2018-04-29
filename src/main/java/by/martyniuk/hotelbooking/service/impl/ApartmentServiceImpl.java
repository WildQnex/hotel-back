package by.martyniuk.hotelbooking.service.impl;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.ApartmentService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The Class ApartmentServiceImpl.
 */
@Service
public class ApartmentServiceImpl implements ApartmentService {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LogManager.getLogger(ApartmentServiceImpl.class);

    /**
     * The apartment dao.
     */
    private ApartmentDao apartmentDao;
    /**
     * The reservation dao.
     */
    private ReservationDao reservationDao;

    @Autowired
    public void setApartmentDao(ApartmentDao apartmentDao) {
        this.apartmentDao = apartmentDao;
    }

    @Autowired
    public void setReservationDao(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

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
        try {
            for (Reservation reservation : reservations) {
                List<Apartment> apartments = apartmentDao.findApartmentListByClassId(reservation.getApartment().getApartmentClass().getId());
                freeApartments.put(reservation, apartments.stream()
                        .filter(apartment -> {
                            try {
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