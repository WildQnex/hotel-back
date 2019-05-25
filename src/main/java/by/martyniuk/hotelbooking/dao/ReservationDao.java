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

/**
 * The Interface ReservationDao.
 */
public interface ReservationDao {

    /**
     * Adds the reservation.
     *
     * @param apartment    the apartment
     * @param user         the user
     * @param checkInDate  the check in date
     * @param checkOutDate the check out date
     * @param totalCost    the total cost
     * @param personAmount the person amount
     * @return true, if successful
     * @throws DaoException the dao exception
     */
    boolean addReservation(Apartment apartment, User user, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal totalCost, int personAmount) throws DaoException;

    /**
     * Checks if is apartment available.
     *
     * @param apartment    the apartment
     * @param checkInDate  the check in date
     * @param checkOutDate the check out date
     * @return true, if is apartment available
     * @throws DaoException the dao exception
     */
    boolean isApartmentAvailable(long apartment, LocalDate checkInDate, LocalDate checkOutDate) throws DaoException;

    /**
     * Read all reservations.
     *
     * @return the list
     * @throws DaoException the dao exception
     */
    List<Reservation> readAllReservations() throws DaoException;

    /**
     * Read all reservations by status.
     *
     * @param status the status
     * @return the list
     * @throws DaoException the dao exception
     */
    List<Reservation> readAllReservationsByStatus(Status status) throws DaoException;

    /**
     * Read all reservations by user id.
     *
     * @param userId the user id
     * @return the list
     * @throws DaoException the dao exception
     */
    List<Reservation> readAllReservationsByUserId(long userId) throws DaoException;

    /**
     * Update reservation apartment and status.
     *
     * @param reservation the reservation
     * @param status      the status
     * @return true, if successful
     * @throws DaoException the dao exception
     */
    boolean updateReservationApartmentAndStatus(Reservation reservation, Status status) throws DaoException;

    /**
     * Read reservation by id.
     *
     * @param id the id
     * @return the optional
     * @throws DaoException the dao exception
     */
    Optional<Reservation> readReservationById(long id) throws DaoException;

    boolean updateReservation(Reservation reservation) throws DaoException;

    boolean deleteReservation(long id) throws DaoException;
}
