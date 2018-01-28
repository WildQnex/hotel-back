package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface ApartmentDao {
    List<Apartment> findAllApartments() throws DaoException;

    Optional<Apartment> findApartmentById(long id) throws DaoException;

    List<Apartment> findApartmentListByClassId(long id) throws DaoException;

    boolean addApartment(Apartment apartment) throws DaoException;

    boolean updateApartment(Apartment apartment) throws DaoException;

    boolean deleteApartment(long apartmentId) throws DaoException;
}
