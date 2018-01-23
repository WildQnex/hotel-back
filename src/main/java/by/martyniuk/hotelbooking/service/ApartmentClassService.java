package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.ApartmentClassDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentClassDaoImpl;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;

import java.util.List;

public class ApartmentClassService {

    public static ApartmentClass findApartmentClassByType(String type) throws ServiceException {
        try {
            ApartmentClassDao dao = new ApartmentClassDaoImpl();

            return dao.findApartmentClassByType(type);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public static ApartmentClass findApartmentClassById(long id) throws ServiceException {
        try {
            ApartmentClassDao dao = new ApartmentClassDaoImpl();

            return dao.findApartmentClassById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public static List<ApartmentClass> findAllApartmentClasses() throws ServiceException {
        try {
            ApartmentClassDao dao = new ApartmentClassDaoImpl();
            return dao.findAllApartmentClasses();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
