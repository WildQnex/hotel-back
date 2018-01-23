package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.DaoException;

import java.util.List;

public interface ApartmentClassDao {

    List<ApartmentClass> findAllApartmentClasses() throws DaoException;

    ApartmentClass findApartmentClassById(long id) throws DaoException;

    ApartmentClass findApartmentClassByType(String type) throws DaoException;
}
