package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface ApartmentClassService {

    Optional<ApartmentClass> findApartmentClassById(long id) throws ServiceException;

    List<ApartmentClass> findAllApartmentClasses() throws ServiceException;
}
