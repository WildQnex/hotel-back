package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.util.List;

public interface ApartmentDao {
    List<Apartment> findAllApartments() throws DaoException;

    Apartment findApartmentById(long id) throws DaoException;

    List<Apartment> findApartmentListByClassId(long id) throws DaoException;

    boolean addApartment(Apartment apartment) throws DaoException;

    boolean updateApartment(Apartment apartment) throws DaoException;

    boolean deleteApartment(long apartmentId) throws DaoException;
}
