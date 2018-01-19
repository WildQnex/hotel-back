package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import by.martyniuk.hotelbooking.dao.impl.ReservationDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationService {
    public static boolean bookApartment(User user, long apartmentId, LocalDate checkInDate, LocalDate checkOutDate, int personsAmount) throws ServiceException {
        ReservationDao dao = new ReservationDaoImpl();
        boolean result = false;
        try {

            ApartmentDao apartmentDao = new ApartmentDaoImpl();
            Apartment apartment = apartmentDao.findApartmentById(apartmentId);
            BigDecimal totalCost = apartment.getApartmentClass().getCostPerPerson().multiply(new BigDecimal(personsAmount));
            totalCost = totalCost.add((new BigDecimal(ChronoUnit.DAYS.between(checkInDate, checkOutDate))).multiply(apartment.getApartmentClass().getCostPerNight()));
            if (apartment.isAnimalsAllowed()) {
                totalCost = totalCost.add(apartment.getApartmentClass().getAnimalCost());
            }
            BigDecimal newBalance = user.getBalance().subtract(totalCost);
            System.out.println(dao.isApartmentAvailable(apartment, checkInDate, checkOutDate));
            System.out.println(newBalance.compareTo(new BigDecimal(0)) > 0);
            System.out.println(apartment.getApartmentClass().getMaxCapacity() >= personsAmount);
            if (dao.isApartmentAvailable(apartment, checkInDate, checkOutDate)
                    && newBalance.compareTo(new BigDecimal(0)) > 0
                    && apartment.getApartmentClass().getMaxCapacity() >= personsAmount) {
                result = dao.addReservation(apartment, user, checkInDate, checkOutDate, totalCost, personsAmount);
            }
            if (result) {
                user.setBalance(newBalance);
            }
            return result;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
