package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    boolean addReservation(Apartment apartment, User user, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal totalCost, int personAmount) throws DaoException;

    boolean isApartmentAvailable(Apartment apartment, LocalDate checkInDate, LocalDate checkOutDate) throws DaoException;

    List<Reservation> readAllReservations() throws DaoException;

    List<Reservation> readAllReservationsByStatus(Status status) throws DaoException;

    List<Reservation> readAllReservationsByUserId(long userId) throws DaoException;

    boolean updateReservationStatus(Reservation reservation, Status status) throws DaoException;

    Optional<Reservation> readReservationById(long id) throws DaoException;
}
