package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * The Interface ApartmentClassService.
 */
public interface ApartmentClassService {

    /**
     * Find apartment class by id.
     *
     * @param id the id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<ApartmentClass> findApartmentClassById(long id) throws ServiceException;

    /**
     * Find all apartment classes.
     *
     * @return the list
     * @throws ServiceException the service exception
     */
    List<ApartmentClass> findAllApartmentClasses() throws ServiceException;

    boolean updateApartmentClass(ApartmentClass apartmentClass) throws ServiceException;

    boolean addApartmentClass(ApartmentClass apartmentClass) throws ServiceException;

    boolean deleteApartmentClass(long id) throws ServiceException;
}
