package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.dao.UserDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import by.martyniuk.hotelbooking.dao.impl.ReservationDaoImpl;
import by.martyniuk.hotelbooking.dao.impl.UserDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationService {
    public static boolean bookApartment(User user, long apartmentId, LocalDate checkInDate, LocalDate checkOutDate, int personsAmount) throws ServiceException {

        boolean result = false;
        try {
            ReservationDao reservationDao = new ReservationDaoImpl();
            ApartmentDao apartmentDao = new ApartmentDaoImpl();
            Apartment apartment = apartmentDao.findApartmentById(apartmentId);
            BigDecimal totalCost = apartment.getApartmentClass().getCostPerPerson().multiply(new BigDecimal(personsAmount));
            totalCost = totalCost.add((new BigDecimal(ChronoUnit.DAYS.between(checkInDate, checkOutDate))).multiply(apartment.getApartmentClass().getCostPerNight()));
            if (apartment.isAnimalsAllowed()) {
                totalCost = totalCost.add(apartment.getApartmentClass().getAnimalCost());
            }
            BigDecimal newBalance = user.getBalance().subtract(totalCost);
            if (reservationDao.isApartmentAvailable(apartment, checkInDate, checkOutDate)
                    && newBalance.compareTo(new BigDecimal(0)) > 0
                    && apartment.getApartmentClass().getMaxCapacity() >= personsAmount) {
                result = reservationDao.addReservation(apartment, user, checkInDate, checkOutDate, totalCost, personsAmount);
            }
            if (result) {
                user.setBalance(newBalance);
                UserDao userDao = new UserDaoImpl();
                userDao.updateUser(user);
            }
            return result;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
