package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {

    boolean addReservation(Apartment apartment, User user, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal totalCost, int personAmount) throws DaoException;

    boolean isApartmentAvailable(Apartment apartment, LocalDate checkInDate, LocalDate checkOutDate) throws DaoException;

    List<Reservation> readAllReservations() throws DaoException;

    List<Reservation> readAllReservationsByStatus(Status status) throws DaoException;

    List<Reservation> readAllReservationsByUserId(long userId) throws DaoException;

    boolean updateReservation(Reservation reservation) throws DaoException;

    Reservation readReservationById(long id) throws DaoException;
}
