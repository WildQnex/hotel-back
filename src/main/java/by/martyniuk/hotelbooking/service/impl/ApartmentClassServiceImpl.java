package by.martyniuk.hotelbooking.service.impl;

import by.martyniuk.hotelbooking.dao.ApartmentClassDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentClassDaoImpl;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.ApartmentClassService;

import java.util.List;
import java.util.Optional;

public class ApartmentClassServiceImpl implements ApartmentClassService {

    @Override
    public Optional<ApartmentClass> findApartmentClassByType(String type) throws ServiceException {
        try {
            ApartmentClassDao dao = new ApartmentClassDaoImpl();

            return dao.findApartmentClassByType(type);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<ApartmentClass> findApartmentClassById(long id) throws ServiceException {
        try {
            ApartmentClassDao dao = new ApartmentClassDaoImpl();

            return dao.findApartmentClassById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ApartmentClass> findAllApartmentClasses() throws ServiceException {
        try {
            ApartmentClassDao dao = new ApartmentClassDaoImpl();
            return dao.findAllApartmentClasses();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
