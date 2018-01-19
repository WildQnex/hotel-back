package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;

public class ApartmentService {
    public static Apartment getApartment(long id) throws ServiceException {
        try {
            ApartmentDao dao = new ApartmentDaoImpl();
            return dao.findApartmentById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
