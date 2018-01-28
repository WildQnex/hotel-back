package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface ApartmentClassDao {

    List<ApartmentClass> findAllApartmentClasses() throws DaoException;

    Optional<ApartmentClass> findApartmentClassById(long id) throws DaoException;
}
