package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The Interface ReservationService.
 */
public interface ReservationService {

    Optional<Reservation> readReservation(long id);

    List<Reservation> readAllReservations();

    boolean deleteReservation(long id);

    boolean updateReservation(Reservation reservation);

    /**
     * Book apartment.
     *
     * @param user             the user
     * @param apartmentClassId the apartment class id
     * @param checkInDate      the check in date
     * @param checkOutDate     the check out date
     * @param personsAmount    the persons amount
     * @return true, if successful
     * @throws ServiceException the service exception
     */
    boolean bookApartment(User user, long apartmentClassId, LocalDate checkInDate, LocalDate checkOutDate, int personsAmount) throws ServiceException;

    /**
     * Read all reservation by user id.
     *
     * @param userId the user id
     * @return the list
     * @throws ServiceException the service exception
     */
    List<Reservation> readAllReservationByUserId(long userId) throws ServiceException;

    /**
     * Read all reservation by status.
     *
     * @param status the status
     * @return the list
     * @throws ServiceException the service exception
     */
    List<Reservation> readAllReservationByStatus(Status status) throws ServiceException;

    /**
     * Update reservation status.
     *
     * @param reservationId the reservation id
     * @param apartmentId   the apartment id
     * @param status        the status
     * @return true, if successful
     * @throws ServiceException the service exception
     */
    boolean updateReservationStatus(long reservationId, long apartmentId, Status status) throws ServiceException;
}
