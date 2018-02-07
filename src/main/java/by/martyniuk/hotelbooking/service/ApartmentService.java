package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The Interface ApartmentService.
 */
public interface ApartmentService {

    /**
     * Insert apartment.
     *
     * @param apartment the apartment
     * @return true, if successful
     * @throws ServiceException the service exception
     */
    boolean insertApartment(Apartment apartment) throws ServiceException;

    /**
     * Update apartment.
     *
     * @param apartment the apartment
     * @return true, if successful
     * @throws ServiceException the service exception
     */
    boolean updateApartment(Apartment apartment) throws ServiceException;

    /**
     * Delete apartment.
     *
     * @param apartmentId the apartment id
     * @return true, if successful
     * @throws ServiceException the service exception
     */
    boolean deleteApartment(long apartmentId) throws ServiceException;

    /**
     * Gets the apartment.
     *
     * @param id the id
     * @return the apartment
     * @throws ServiceException the service exception
     */
    Optional<Apartment> getApartment(long id) throws ServiceException;

    /**
     * Find free apartments for reservations.
     *
     * @param reservations the reservations
     * @return the map
     * @throws ServiceException the service exception
     */
    Map<Reservation, List<Apartment>> findFreeApartmentsForReservations(List<Reservation> reservations) throws ServiceException;

    /**
     * Find all apartments.
     *
     * @return the list
     * @throws ServiceException the service exception
     */
    List<Apartment> findAllApartments() throws ServiceException;
}
