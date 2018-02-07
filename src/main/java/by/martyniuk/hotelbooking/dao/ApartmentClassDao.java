package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * The Interface ApartmentClassDao.
 */
public interface ApartmentClassDao {

    /**
     * Find all apartment classes.
     *
     * @return the list
     * @throws DaoException the dao exception
     */
    List<ApartmentClass> findAllApartmentClasses() throws DaoException;

    /**
     * Find apartment class by id.
     *
     * @param id the id
     * @return the optional
     * @throws DaoException the dao exception
     */
    Optional<ApartmentClass> findApartmentClassById(long id) throws DaoException;
}
