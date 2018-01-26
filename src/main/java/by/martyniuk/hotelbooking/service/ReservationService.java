package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    boolean bookApartment(User user, long apartmentClassId, LocalDate checkInDate, LocalDate checkOutDate, int personsAmount) throws ServiceException;

    List<Reservation> readAllReservationByUserId(long userId) throws ServiceException;

    List<Reservation> readAllReservationByStatus(Status status) throws ServiceException;

    boolean updateReservation(Reservation reservation) throws ServiceException;

    boolean approveReservation(long reservationId, long apartmentId, Status status) throws ServiceException;
}
