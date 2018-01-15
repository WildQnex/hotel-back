package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.util.List;

public interface ApartmentDao {
    List<Apartment> findAllApartments() throws DaoException;

    Apartment findApartmentByNumber(String number) throws DaoException;

    boolean addApartment(Apartment user) throws DaoException;
}
