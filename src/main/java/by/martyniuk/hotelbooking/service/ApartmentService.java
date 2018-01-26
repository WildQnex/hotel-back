package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface ApartmentService {
    boolean insertApartment(Apartment apartment) throws ServiceException;

    boolean updateApartment(Apartment apartment) throws ServiceException;

    boolean deleteApartment(long apartmentId) throws ServiceException;

    Apartment getApartment(long id) throws ServiceException;

    Map<Reservation, List<Apartment>> findFreeApartmentsForReservations(List<Reservation> reservations) throws ServiceException;
}
