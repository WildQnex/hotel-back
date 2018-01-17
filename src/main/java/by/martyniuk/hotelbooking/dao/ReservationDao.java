package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.time.LocalDate;

public interface ReservationDao {

    boolean addReservation(Apartment apartment, User user, LocalDate checkInDate, LocalDate checkOutDate, int personsAmount) throws DaoException;

    boolean isApartmentAvailable(Apartment apartment, LocalDate checkInDate, LocalDate checkOutDate) throws DaoException;
}
